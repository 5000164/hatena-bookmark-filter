package domain

import java.time.LocalDateTime

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

  /** 対象の URL を条件に応じて結果を判定する。
    *
    * ブックマーク数は投稿する時にも使用するので 2 回取得しなくてもいいように判定のために取得した値を返す。
    *
    * @param url                対象の URL
    * @param now                現在日時
    * @param createdAt          対象の URL が保存された日時
    * @param waitSeconds        URL を保存してから投稿するまでの待ち時間
    * @param fetchBookmarkCount 対象の URL のブックマーク数を取得する処理
    * @param threshold          しきい値となるブックマーク数
    * @return 条件に応じた結果
    */
  def judge(url: String, now: LocalDateTime, createdAt: LocalDateTime, waitSeconds: Int, fetchBookmarkCount: String => Int, threshold: Int): (JudgeType, Option[Int]) = {
    // 指定した時間分を経過した記事だけ対象にする
    if (createdAt.isBefore(now.minusSeconds(waitSeconds))) {
      val bookmarkCount = fetchBookmarkCount(url)
      // 指定したブックマーク数を超えた記事だけ対象にする
      if (bookmarkCount >= threshold) {
        (Qualified, Some(bookmarkCount))
      } else {
        (NotQualified, None)
      }
    } else {
      (Still, None)
    }
  }
}

sealed trait JudgeType

case object Qualified extends JudgeType

case object NotQualified extends JudgeType

case object Still extends JudgeType
