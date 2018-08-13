package domain

import java.time.LocalDateTime

import infrastructure.WatchSettings
import interfaces.{Client, HatenaBookmark}

/** 記事を表現する。
  *
  * @param url           URL
  * @param title         タイトル
  * @param bookmarkCount ブックマーク数
  * @param commentUrl    コメントページの URL
  * @param postChannelId Slack の投稿先チャンネル
  * @param userName      Slack での投稿名
  * @param iconEmoji     Slack での表示アイコン
  */
case class Article(
    url: String,
    title: String,
    bookmarkCount: Int,
    commentUrl: String,
    postChannelId: String,
    userName: String,
    iconEmoji: String) {

  /** Slack への投稿用に整形する。
    *
    * @return Slack へ投稿する内容
    */
  def toSlackString: String =
    s""":bookmark: $bookmarkCount
       |$title
       |$url
       |$commentUrl""".stripMargin
}

/** Article のコンパニオンオブジェクト。 */
object Article {
  /**
    * commentUrl は渡されてから構築する。
    *
    * @param url           URL
    * @param title         タイトル
    * @param bookmarkCount ブックマーク数
    * @param postChannelId Slack の投稿先チャンネル
    * @param userName      Slack での投稿名
    * @param iconEmoji     Slack での表示アイコン
    * @return commentUrl を構築して付与した Article
    */
  def apply(
      url: String,
      title: String,
      bookmarkCount: Int,
      postChannelId: String,
      userName: String,
      iconEmoji: String): Article = new Article(url, title, bookmarkCount, HatenaBookmark.buildCommentUrl(url), postChannelId, userName, iconEmoji)

  /** 条件を満たしていた場合に記事情報を構築する。
    *
    * @param url           記事の構築とブックマーク数の判定に使用する
    * @param watchSettings 記事の構築と判定条件の取得に使用する
    * @param createdAt     対象の URL が保存されてから指定した時間後に判定するために URL が保存された日時を使用する
    * @return 条件を満たしていた場合のみ記事情報
    */
  def buildIfQualified(url: String, watchSettings: WatchSettings, createdAt: LocalDateTime): Option[Article] = {
    if (createdAt.isBefore(LocalDateTime.now().minusSeconds(watchSettings.waitSeconds))) {
      val bookmarkCount = HatenaBookmark.fetchBookmarkCount(url)
      if (bookmarkCount >= watchSettings.threshold) {
        val title = Client.fetchTitle(url)
        Some(Article(url, title, bookmarkCount, watchSettings.slack.postChannelId, watchSettings.slack.userName, watchSettings.slack.iconEmoji))
      } else {
        None
      }
    } else {
      None
    }
  }
}
