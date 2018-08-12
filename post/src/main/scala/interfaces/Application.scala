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
      unprocessedList <- repository.fetchAllUnprocessed().grouped(settings.parallelPostCount)
    } Await.ready(Future.sequence(for {
      (url, settingsId, createdAt) <- unprocessedList
    } yield {
      val f = Future {
        Article.buildIfQualified(url, settings.watches(settingsId), createdAt).foreach { article =>
          Slack.post(settings.slackToken, article).toOption.foreach { _ =>
            repository.processed(url, settingsId) match {
              case Right(_) =>
              case Left(e) => logger.error(s"保存処理に失敗 url:$url, settingsId:$settingsId", e)
            }
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
