package domain

import scala.xml.{Elem, Node, NodeSeq, XML}

/**
  * 抽出する処理を行う。
  */
object Extractor {
  /**
    * ページ一覧を抽出する。
    *
    * @param xmlString 抽出対象の XML 形式の文字列
    * @return 抽出結果
    */
  def extractPage(xmlString: String): Seq[DeliveredPage] = {
    val xml = XML.loadString(xmlString)
    getItems(xml).map(getPage)
  }

  /**
    * 処理対象の一覧を取得する。
    *
    * @param element 抽出対象の XML
    * @return 処理対象の一覧
    */
  private def getItems(element: Elem): NodeSeq = element \ "item"

  /**
    * ページを抽出する。
    *
    * @param node 抽出対象のノード
    * @return 抽出したページ
    */
  private def getPage(node: Node): DeliveredPage = {
    val url = (node \ "link").text
    val date = (node \ "date").text
    DeliveredPage(url, date)
  }
}
