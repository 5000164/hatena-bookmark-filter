package interfaces

import com.typesafe.scalalogging.LazyLogging
import domain.Articles
import infrastructure.Repository
import infrastructure.Settings.settings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** アプリを起動する。 */
object Application extends App with LazyLogging {
  logger.info("collect 実行開始")

  val repository = new Repository()
  try {
    Await.ready(Future.sequence(for {
      (settingsId, watchSettings) <- settings.watches
      deliveredArticle <- Articles.fetchDeliveredArticles(watchSettings.feedUrl, Client.fetchContent)
    } yield Future {
      if (!repository.existsUrl(deliveredArticle.url, settingsId)) repository.save(deliveredArticle.url, settingsId) match {
        case Right(_) =>
        case Left(e) => logger.error(s"保存処理に失敗 article:$deliveredArticle, settingsId:$settingsId", e)
      }
    }), Duration.Inf)
  } catch {
    case e: Throwable =>
      logger.error("エラー発生", e)
      throw e
  } finally repository.close()

  logger.info("collect 実行終了")
}
