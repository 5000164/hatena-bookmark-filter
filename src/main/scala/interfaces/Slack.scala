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
    * @param token    接続する Bot ユーザーのトークン
    * @param pageList 投稿内容
    */
  def post(token: String, pageList: Seq[Page]): Unit = {
    implicit val system: ActorSystem = ActorSystem("slack")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    val client = BlockingSlackApiClient(token)
    pageList.foreach(page => {
      val message = s"はてなブックマーク数: ${page.hatenaBookmarkCount}\n${page.url}\n${page.commentUrl}"
      client.postChatMessage(
        channelId = page.postChannelId,
        text = message,
        unfurlLinks = Some(true),
        username = Some(page.userName),
        iconEmoji = Some(page.iconEmoji))
    })

    Http().shutdownAllConnectionPools().onComplete { _ =>
      system.terminate
    }
  }
}
