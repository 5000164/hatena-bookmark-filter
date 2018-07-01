package domain

import scala.xml.{Elem, NodeSeq, XML}

/**
  * 抽出する処理を行う
  */
object Extractor {
  /**
    * URL を抽出する
    *
    * @param xmlString 抽出対象の XML 形式の文字列
    * @return 抽出結果
    */
  def extractUrl(xmlString: String): Seq[String] = {
    val xml = XML.loadString(xmlString)
    getItems(xml).map(_.toString).map(getUrl).collect { case Right(r) => r }
  }

  /**
    * 処理対象の一覧を取得する
    *
    * @param element 抽出対象の XML
    * @return 処理対象の一覧
    */
  private def getItems(element: Elem): NodeSeq = element \ "channel" \ "items" \ "Seq" \ "li"

  /**
    * URL を抽出する
    *
    * @param raw 抽出対象の文字列
    * @return 抽出した URL
    */
  private def getUrl(raw: String): Either[Unit, String] = {
    val r = """.*rdf:resource="(.*?)".*""".r
    r.findFirstMatchIn(raw).map(_.group(1)).toRight(())
  }
}
