package interfaces

import com.typesafe.scalalogging.LazyLogging
import domain.Article
import infrastructure.Repository
import infrastructure.Settings.settings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** アプリを起動する。 */
object Application extends App with LazyLogging {
  logger.info("実行開始")

  val repository = new Repository()
  try {
    for {
      unpostedList <- repository.fetchAllUnposted().grouped(settings.parallelPostCount)
    } Await.ready(Future.sequence(for {
      (url, settingsId) <- unpostedList
    } yield {
      val f = Future {
        val title = Client.fetchTitle(url)
        val bookmarkCount = HatenaBookmark.fetchBookmarkCount(url)
        val watchSettings = settings.watches(settingsId)
        (if (bookmarkCount >= watchSettings.threshold) {
          val article = Article(url, title, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji)
          Slack.post(settings.slackToken, article)
        } else {
          Right("")
        }).toOption.foreach { _ =>
          repository.posted(url, settingsId) match {
            case Right(_) =>
            case Left(e) => logger.error(s"保存処理に失敗 url:$url, settingsId:$settingsId", e)
          }
        }
      }
      f.failed.foreach(e => logger.error("エラー発生", e))
      f
    }), Duration.Inf)
  } catch {
    case e: Throwable =>
      logger.error("エラー発生", e)
      throw e
  } finally repository.close()

  logger.info("実行終了")
}
