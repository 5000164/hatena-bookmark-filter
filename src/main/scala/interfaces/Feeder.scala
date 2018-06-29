package interfaces

import com.softwaremill.sttp._
import domain.Extractor.extractUrl

import scala.xml.XML

/**
  * RSS に関する処理を行う
  */
object Feeder {
  /**
    * 配信された URL の一覧を取得する
    *
    * @return 配信された URL の一覧
    */
  def fetchUrlList(): Seq[String] = {
    val request = sttp.get(uri"http://b.hatena.ne.jp/hotentry/all.rss")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val xmlString = response.body.getOrElse("")
    val xml = XML.loadString(xmlString)
    (xml \ "channel" \ "items" \ "Seq" \ "li").map(_.toString).map(extractUrl).collect { case Right(r) => r }
  }
}
