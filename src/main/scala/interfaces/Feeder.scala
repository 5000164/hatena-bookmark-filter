package interfaces

import java.io.File

import com.softwaremill.sttp._
import domain.Extractor.extractArticle
import domain.{Article, DeliveredArticle}
import infrastructure.{SlackSettings, WatchSettings}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * RSS に関する処理を行う。
  */
object Feeder {
  /**
    * 条件を満たした記事の一覧を取得する。
    *
    * @param watches 購読対象の設定一覧
    * @return 条件を満たした記事の一覧
    */
  def fetchArticleList(watches: Seq[WatchSettings]): Seq[Article] = {
    val databasePath = "./db.db"
    class Articles(tag: Tag) extends Table[(Int, String)](tag, "ARTICLES") {
      def id = column[Int]("ARTICLE_ID", O.AutoInc, O.PrimaryKey)
      def url = column[String]("URL")
      def * = (id, url)
    }
    val articles = TableQuery[Articles]

    val db = Database.forURL(s"jdbc:sqlite:$databasePath", driver = "org.sqlite.JDBC")
    try {
      if (!new File(databasePath).exists()) {
        val setup = DBIO.seq(
          articles.schema.create
        )
        db.run(setup)
      }

      watches.flatMap(watchSettings => {
        val deliveredArticleList = fetchDeliveredArticleList(watchSettings.feedUrl)
        val filtered = filter(deliveredArticleList, watchSettings.threshold, watchSettings.slack).filter(f => {
          val q = articles.filter(_.url === f.url).exists
          val action = q.result
          val result = db.run(action)
          !Await.result(result, Duration.Inf)
        })

        filtered.foreach(p => {
          val insertActions = DBIO.seq(
            articles += (0, p.url)
          )
          Await.result(db.run(insertActions), Duration.Inf)
        })

        filtered
      })
    } finally db.close
  }

  /**
    * 配信された記事の一覧を取得する。
    *
    * @return 配信された記事の一覧
    */
  private def fetchDeliveredArticleList(feedUrl: String): Seq[DeliveredArticle] = {
    val request = sttp.get(uri"$feedUrl")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val xmlString = response.body.getOrElse("")
    extractArticle(xmlString)
  }

  /**
    * 条件を元に絞り込む。
    *
    * @param articleList  調査する対象の記事一覧
    * @param threshold しきい値とするはてなブックマーク件数
    * @param slackSettings Slack に投稿する際の情報
    * @return 絞り込んだ後の記事一覧
    */
  private def filter(articleList: Seq[DeliveredArticle], threshold: Int, slackSettings: SlackSettings): Seq[Article] = {
    articleList.map(article => {
      val starCount = fetchStarCount(article.url)
      val commentUrl = buildCommentUrl(article.url)
      Article(article.url, article.date, starCount, commentUrl, slackSettings.postChannelId, slackSettings.userName, slackSettings.iconEmoji)
    }).filter(_.hatenaBookmarkCount > threshold)
  }

  /**
    * 対象の URL のはてなブックマーク件数を取得する。
    *
    * @param url 調査する対象の URL
    * @return 対象の URL のはてなブックマーク件数
    */
  private def fetchStarCount(url: String): Int = {
    val request = sttp.get(uri"http://api.b.st-hatena.com/entry.count?${Map("url" -> url)}")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    response.body.getOrElse("0").toInt
  }

  /**
    * はてなブックマークコメントページの URL を構築する
    *
    * @param url この URL へのコメントページの URL を構築する
    * @return はてなブックマークコメントページの URL
    */
  private def buildCommentUrl(url: String): String = {
    val connector = if (url(4) == 's') {"s/"} else {""}
    val path = url.split(':')(1).drop(2)
    s"http://b.hatena.ne.jp/entry/$connector$path"
  }
}
