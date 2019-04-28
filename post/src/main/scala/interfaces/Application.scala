package interfaces

import java.time.LocalDateTime

import com.typesafe.scalalogging.LazyLogging
import domain.{Article, NotQualified, Qualified, Still}
import infrastructure.Repository
import infrastructure.Settings.settings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** アプリを起動する。 */
object Application extends App with LazyLogging {
  logger.info("post 実行開始")

  val repository = new Repository()
  try {
    val now = LocalDateTime.now()
    for {
      unprocessedList <- repository.fetchAllUnprocessed().grouped(settings.parallelPostCount)
    } Await.ready(
      Future.sequence(for {
        (id, url, settingsId, createdAt) <- unprocessedList
      } yield {
        val f = Future {
          settings.watches.get(settingsId).foreach {
            watchSettings =>
              (Article.judge(url, now, createdAt, watchSettings.waitSeconds, HatenaBookmark.fetchBookmarkCount, watchSettings.threshold) match {
                case (Qualified, Some(bookmarkCount)) =>
                  val title = Client.fetchTitle(url)
                  val article =
                    Article(url, title, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji)
                  Some(Slack.post(settings.slackToken, article))
                case (NotQualified, None) => Some(Right(""))
                case (Still, None)        => None
                case _                    => Some(Left(new Exception("想定していない値")))
              }).foreach {
                case Right(_) =>
                  repository.markProcessed(id) match {
                    case Right(_) =>
                    case Left(e)  => logger.error(s"保存処理に失敗 url:$url, settingsId:$settingsId", e)
                  }
                case Left(e) => logger.error("エラー発生", e)
              }
          }
        }
        f.failed.foreach(e => logger.error("エラー発生", e))
        f
      }),
      Duration.Inf
    )
  } catch {
    case e: Throwable =>
      logger.error("エラー発生", e)
      throw e
  } finally repository.close()

  logger.info("post 実行終了")
}
