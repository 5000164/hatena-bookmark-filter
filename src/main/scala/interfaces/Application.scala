package interfaces

import domain.{Article, Extractor, Judge}
import infrastructure.Settings.settings
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** アプリを起動する。 */
object Application extends App {
  val db = Database.forURL("jdbc:sqlite:./db.db", driver = "org.sqlite.JDBC")
  try {
    Await.ready(Future.sequence(for {
      watchSettings <- settings.watches
      deliveredArticle <- Extractor.fetchDeliveredArticles(watchSettings.feedUrl, Client.fetchContent)
    } yield Future {
      Judge.refine(deliveredArticle.url, watchSettings.threshold, Repository.existsUrl(db, _), HatenaBookmark.fetchBookmarkCount) match {
        case Some(bookmarkCount) =>
          val article = Article(deliveredArticle.url, deliveredArticle.date, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji)
          Slack.post(settings.slackToken, article)
          Repository.save(db, article.url)
        case None =>
      }
    }), Duration.Inf)
  } finally db.close
}
