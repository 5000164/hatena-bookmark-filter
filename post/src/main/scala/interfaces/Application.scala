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
    Await.ready(Future.sequence(for {
      (url, settingsId) <- repository.fetchAllUnposted()
    } yield Future {
      val title = "" // TODO: タイトルを投稿するようにする
      val bookmarkCount = HatenaBookmark.fetchBookmarkCount(url) // TODO: 取得したブックマーク数で絞り込むようにする
      val watchSettings = settings.watches(settingsId)
      val article = Article(url, title, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji)
      Slack.post(settings.slackToken, article).toOption.foreach { _ =>
        repository.posted(url, settingsId) match {
          case Right(_) =>
          case Left(e) => logger.error(s"保存処理に失敗 article:$article, settingsId:$settingsId", e)
        }
      }
    }), Duration.Inf)

  } catch {
    case e: Throwable =>
      logger.error("エラー発生", e)
      throw e
  } finally repository.close()

  logger.info("実行終了")
}
