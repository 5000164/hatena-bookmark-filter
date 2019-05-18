package domain

import scala.xml.{Elem, Node, XML}

/** 記事に関する操作を抽象化して扱う。 */
object Articles {

  /** 配信された記事一覧を取得する。
    *
    * @param feedUrl      抽出元の RSS の URL
    * @param fetchContent 対象の URL の内容を取得する処理
    * @return 抽出した配信記事一覧
    */
  def fetchDeliveredArticles(feedUrl: String, fetchContent: String => Option[String]): Seq[DeliveredArticle] = {
    val getItems = (element: Elem) => element \ "item"
    val getArticle = (node: Node) => {
      val url   = (node \ "link").text
      val title = (node \ "title").text
      DeliveredArticle(url, title)
    }

    fetchContent(feedUrl) match {
      case Some(xmlString) =>
        val xml = XML.loadString(xmlString)
        getItems(xml).map(getArticle)
      case None => Seq()
    }
  }
}

/** RSS で配信された情報を表現する。
  *
  * @param url   URL
  * @param title タイトル
  */
case class DeliveredArticle(url: String, title: String)
