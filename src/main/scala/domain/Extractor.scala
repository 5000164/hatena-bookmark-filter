package domain

import scala.xml.{Elem, Node, NodeSeq, XML}

/**
  * 抽出する処理を行う。
  */
object Extractor {
  /**
    * 記事一覧を抽出する。
    *
    * @param xmlString 抽出対象の XML 形式の文字列
    * @return 抽出した記事一覧
    */
  def extractArticle(xmlString: String): Seq[DeliveredArticle] = {
    val xml = XML.loadString(xmlString)
    getItems(xml).map(getArticle)
  }

  /**
    * 処理対象の一覧を取得する。
    *
    * @param element 抽出対象の XML
    * @return 処理対象の一覧
    */
  private def getItems(element: Elem): NodeSeq = element \ "item"

  /**
    * 記事を抽出する。
    *
    * @param node 抽出対象のノード
    * @return 抽出した記事
    */
  private def getArticle(node: Node): DeliveredArticle = {
    val url = (node \ "link").text
    val date = (node \ "date").text
    DeliveredArticle(url, date)
  }
}
