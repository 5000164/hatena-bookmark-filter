package interfaces

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

import com.softwaremill.sttp._
import domain.Extractor.extractPage
import domain.{DeliveredPage, Page}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * RSS に関する処理を行う。
  */
object Feeder {
  /**
    * 条件を満たした URL の一覧を取得する。
    *
    * @param feedUrl        ページを取得する元の RSS の URL
    * @param threshold      しきい値とするはてなブックマークの件数
    * @param lastExecutedAt 更新されたページのみを取得するため使用する最終実行日時
    * @return 条件を満たした URL の一覧
    */
  def fetchPageList(feedUrl: String, threshold: Int, lastExecutedAt: Option[Date]): Seq[Page] = {

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

      val deliveredPageList = fetchDeliveredPageList(feedUrl)

      deliveredPageList.foreach(p => {
        val insertActions = DBIO.seq(
          articles += (0, p.url)
        )
        Await.result(db.run(insertActions), Duration.Inf)
      })

      val filtered = filter(deliveredPageList, threshold, lastExecutedAt)

      filtered.filter(f => {
        val q = articles.filter(_.url === f.url).exists
        val action = q.result
        val result = db.run(action)
        !Await.result(result, Duration.Inf)
      })
    } finally db.close
  }

  /**
    * 配信されたページの一覧を取得する。
    *
    * @return 配信されたページの一覧
    */
  private def fetchDeliveredPageList(feedUrl: String): Seq[DeliveredPage] = {
    val request = sttp.get(uri"$feedUrl")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val xmlString = response.body.getOrElse("")
    extractPage(xmlString)
  }

  /**
    * 条件を元に絞り込む。
    *
    * @param pageList       調査する対象のページ一覧
    * @param threshold      しきい値とするはてなブックマーク件数
    * @param lastExecutedAt 更新されたページのみを取得するため使用する最終実行日時
    * @return 絞り込んだ後の URL の一覧
    */
  private def filter(pageList: Seq[DeliveredPage], threshold: Int, lastExecutedAt: Option[Date]): Seq[Page] = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
    pageList.map(page => {
      val starCount = fetchStarCount(page.url)
      val commentUrl = buildCommentUrl(page.url)
      Page(page.url, page.date, starCount, commentUrl)
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
