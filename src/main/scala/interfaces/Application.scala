package interfaces

import com.softwaremill.sttp._
import domain.Extractor.extractUrl

import scala.xml.XML

object Application extends App {
  val request = sttp.get(uri"http://b.hatena.ne.jp/hotentry/all.rss")
  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
  val response = request.send()
  val xmlString = response.body.getOrElse("")
  val xml = XML.loadString(xmlString)
  (xml \ "channel" \ "items" \ "Seq" \ "li").map(_.toString).map(extractUrl).foreach(println)
}
