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
    */
  def post(token: String, article: Article): Unit = {
    implicit val system: ActorSystem = ActorSystem("slack")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    val client = BlockingSlackApiClient(token)
    client.postChatMessage(
      channelId = article.postChannelId,
      text = article.toSlackString,
      unfurlLinks = Some(true),
      username = Some(article.userName),
      iconEmoji = Some(article.iconEmoji))

    Http().shutdownAllConnectionPools().onComplete { _ =>
      system.terminate
    }
  }
}
