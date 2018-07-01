package interfaces

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import slack.api.BlockingSlackApiClient

import scala.concurrent.ExecutionContextExecutor

/**
  * Slack に関する処理を行う
  */
object Slack {
  /**
    * 投稿する
    *
    * @param token         接続する Bot ユーザーのトークン
    * @param postChannelId 投稿するチャンネル ID
    * @param userName      表示するユーザー名
    * @param iconEmoji     表示するアイコン
    * @param messages      投稿内容
    */
  def post(token: String, postChannelId: String, userName: String, iconEmoji: String, messages: Seq[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("slack")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    val client = BlockingSlackApiClient(token)
    messages.foreach(message =>
      client.postChatMessage(
        channelId = postChannelId,
        text = message,
        unfurlLinks = Some(true),
        username = Some(userName),
        iconEmoji = Some(iconEmoji)))

    Http().shutdownAllConnectionPools().onComplete { _ =>
      system.terminate
    }
  }
}
