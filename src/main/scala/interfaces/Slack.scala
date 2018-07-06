package interfaces

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import domain.Article
import slack.api.BlockingSlackApiClient

import scala.concurrent.ExecutionContextExecutor

/**
  * Slack に関する処理を行う。
  */
object Slack {
  /**
    * 投稿する。
    *
    * @param token       接続する Bot ユーザーのトークン
    * @param articleList 投稿内容
    */
  def post(token: String, articleList: Seq[Article]): Unit = {
    implicit val system: ActorSystem = ActorSystem("slack")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    val client = BlockingSlackApiClient(token)
    articleList.foreach(article => {
      val message = s"はてなブックマーク数: ${article.hatenaBookmarkCount}\n${article.url}\n${article.commentUrl}"
      client.postChatMessage(
        channelId = article.postChannelId,
        text = message,
        unfurlLinks = Some(true),
        username = Some(article.userName),
        iconEmoji = Some(article.iconEmoji))
    })

    Http().shutdownAllConnectionPools().onComplete { _ =>
      system.terminate
    }
  }
}
