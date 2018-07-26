package interfaces

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import domain.Article
import slack.api.BlockingSlackApiClient

import scala.concurrent.ExecutionContextExecutor

/** Slack に関する処理を行う。 */
object Slack {
  /** 投稿する。
    *
    * @param token   接続する Bot ユーザーのトークン
    * @param article 投稿する記事
    * @return 実行結果
    */
  def post(token: String, article: Article): Either[String, String] = {
    implicit val system: ActorSystem = ActorSystem("slack")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    val client = BlockingSlackApiClient(token)
    try {
      Right(client.postChatMessage(
        channelId = article.postChannelId,
        text = article.toSlackString,
        unfurlLinks = Some(true),
        username = Some(article.userName),
        iconEmoji = Some(article.iconEmoji)))
    }
    catch {
      case e: Exception => Left(e.toString)
    }
    finally {
      Http().shutdownAllConnectionPools().onComplete { _ =>
        system.terminate
      }
    }
  }
}
