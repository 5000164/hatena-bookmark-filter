package domain

import scala.xml.{Elem, Node, NodeSeq, XML}

/** 抽出する処理を行う。 */
object Extractor {
  /** 記事一覧を抽出する。
    *
    * @param feedUrl      抽出元の RSS の URL
    * @param fetchContent 対象の URL の内容を取得する処理
    * @return 抽出した配信記事一覧
    */
  def fetchDeliveredArticles(feedUrl: String, fetchContent: String => String): Seq[DeliveredArticle] = {
    val xmlString = fetchContent(feedUrl)
    val xml = XML.loadString(xmlString)
    getItems(xml).map(getArticle)
  }

  /** 処理対象の一覧を取得する。
    *
    * @param element 抽出対象の XML
    * @return 処理対象の一覧
    */
  private def getItems(element: Elem): NodeSeq = element \ "item"

  /** 記事を抽出する。
    *
    * @param node 抽出対象のノード
    * @return 抽出した記事
    */
  private def getArticle(node: Node): DeliveredArticle = {
    val url = (node \ "link").text
    val title = (node \ "title").text
    DeliveredArticle(url, title)
  }
}
