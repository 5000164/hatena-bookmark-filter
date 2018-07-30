package domain

import interfaces.HatenaBookmark

import scala.xml.{Elem, Node, XML}

/** 記事に関する操作を抽象化して扱う。 */
object Articles {
  /** 配信された記事一覧を取得する。
    *
    * @param feedUrl      抽出元の RSS の URL
    * @param fetchContent 対象の URL の内容を取得する処理
    * @return 抽出した配信記事一覧
    */
  def fetchDeliveredArticles(feedUrl: String, fetchContent: String => String): Seq[DeliveredArticle] = {
    val getItems = (element: Elem) => element \ "item"
    val getArticle = (node: Node) => {
      val url = (node \ "link").text
      val title = (node \ "title").text
      DeliveredArticle(url, title)
    }

    val xmlString = fetchContent(feedUrl)
    val xml = XML.loadString(xmlString)
    getItems(xml).map(getArticle)
  }

  /** 投稿する記事だけに絞り込む。
    *
    * @param url                配信された URL
    * @param threshold          ブックマーク数のしきい値とする値
    * @param existsUrl          対象の URL がすでに投稿済みか判断する処理
    * @param fetchBookmarkCount 対象の URL のブックマーク数を取得する処理
    * @return 絞り込んだ結果として、投稿するものは Some、投稿しないものは None を返す、Some の中にはブックマーク数の取得処理を何回も行わないために取得したブックマーク数を返す
    */
  def refine(url: String, threshold: Int, existsUrl: String => Boolean, fetchBookmarkCount: String => Int): Option[Int] = {
    if (existsUrl(url)) None
    else {
      val bookmarkCount = fetchBookmarkCount(url)
      if (bookmarkCount >= threshold) Some(bookmarkCount) else None
    }
  }
}

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
}

/** RSS で配信された情報を表現する。
  *
  * @param url   URL
  * @param title タイトル
  */
case class DeliveredArticle(
    url: String,
    title: String)
