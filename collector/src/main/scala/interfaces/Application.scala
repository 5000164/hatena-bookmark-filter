package interfaces

import com.typesafe.scalalogging.LazyLogging
import domain.{Article, Extractor, Judge}
import infrastructure.Settings.settings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** アプリを起動する。 */
object Application extends App with LazyLogging {
  logger.info("実行開始")

  val repository = new Repository()
  try {
    Await.ready(Future.sequence(for {
      (settingsId, watchSettings) <- settings.watches
      deliveredArticle <- Extractor.fetchDeliveredArticles(watchSettings.feedUrl, Client.fetchContent)
    } yield Future {
      Judge.refine(deliveredArticle.url, watchSettings.threshold, repository.existsUrl, HatenaBookmark.fetchBookmarkCount) match {
        case Some(bookmarkCount) =>
          val article = Article(deliveredArticle.url, deliveredArticle.title, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji)
          Slack.post(settings.slackToken, article).toOption.foreach { _ =>
            repository.save(article.url, settingsId) match {
              case Right(_) =>
              case Left(e) => logger.error(s"保存処理に失敗 article:$article, settingsId:$settingsId", e)
            }
          }
        case None =>
      }
    }), Duration.Inf)
  } finally repository.close()

  logger.info("実行終了")
}
