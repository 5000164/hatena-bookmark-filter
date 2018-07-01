package interfaces

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import domain.Page
import slack.api.BlockingSlackApiClient

import scala.concurrent.ExecutionContextExecutor

/**
  * Slack に関する処理を行う。
  */
object Slack {
  /**
    * 投稿する。
    *
    * @param token         接続する Bot ユーザーのトークン
    * @param postChannelId 投稿するチャンネル ID
    * @param userName      表示するユーザー名
    * @param iconEmoji     表示するアイコン
    * @param pageList      投稿内容
    */
  def post(token: String, postChannelId: String, userName: String, iconEmoji: String, pageList: Seq[Page]): Unit = {
    implicit val system: ActorSystem = ActorSystem("slack")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    val client = BlockingSlackApiClient(token)
    pageList.foreach(page => {
      val message = s"はてなブックマーク数: ${page.hatenaBookmarkCount}\n${page.url}"
      client.postChatMessage(
        channelId = postChannelId,
        text = message,
        unfurlLinks = Some(true),
        username = Some(userName),
        iconEmoji = Some(iconEmoji))
    })

    Http().shutdownAllConnectionPools().onComplete { _ =>
      system.terminate
    }
  }
}
