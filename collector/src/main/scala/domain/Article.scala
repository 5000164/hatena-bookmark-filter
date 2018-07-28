package domain

import interfaces.HatenaBookmark

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
    s"""$title: $bookmarkCount
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
}

/** RSS で配信された情報を表現する。
  *
  * @param url   URL
  * @param title タイトル
  */
case class DeliveredArticle(
    url: String,
    title: String)
