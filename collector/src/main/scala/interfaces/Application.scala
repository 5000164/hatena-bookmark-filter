package interfaces

import com.typesafe.scalalogging.LazyLogging
import domain.{Article, Extractor, Judge}
import infrastructure.Settings.settings
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** アプリを起動する。 */
object Application extends App with LazyLogging {
  logger.info("実行開始")

  val db = Database.forURL("jdbc:sqlite:./db.db", driver = "org.sqlite.JDBC")
  try {
    Await.ready(Future.sequence(for {
      watchSettings <- settings.watches
      deliveredArticle <- Extractor.fetchDeliveredArticles(watchSettings.feedUrl, Client.fetchContent)
    } yield Future {
      Judge.refine(deliveredArticle.url, watchSettings.threshold, Repository.existsUrl(db, _), HatenaBookmark.fetchBookmarkCount) match {
        case Some(bookmarkCount) =>
          val article = Article(deliveredArticle.url, deliveredArticle.date, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji)
          Slack.post(settings.slackToken, article) match {
            case Right(_) => Repository.save(db, article.url)
            case Left(e) => logger.error(s"保存処理に失敗 $article", e)
          }
        case None =>
      }
    }), Duration.Inf)
  } finally db.close

  logger.info("実行終了")
}
