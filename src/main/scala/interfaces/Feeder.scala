package interfaces

import com.softwaremill.sttp._
import domain.Extractor.extractUrl

import scala.xml.XML

/**
  * RSS に関する処理を行う
  */
object Feeder {
  /**
    * 条件を満たした URL の一覧を取得する
    *
    * @return 条件を満たした URL の一覧
    */
  def fetchUrlList(): Seq[String] = {
    val deliveredUrlList = fetchDeliveredUrlList()
    filter(deliveredUrlList, 10)
  }

  /**
    * 配信された URL の一覧を取得する
    *
    * @return 配信された URL の一覧
    */
  def fetchDeliveredUrlList(): Seq[String] = {
    val request = sttp.get(uri"http://b.hatena.ne.jp/hotentry/all.rss")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val xmlString = response.body.getOrElse("")
    val xml = XML.loadString(xmlString)
    (xml \ "channel" \ "items" \ "Seq" \ "li").map(_.toString).map(extractUrl).collect { case Right(r) => r }
  }

  /**
    * 条件を元に絞り込む
    *
    * @param urlList   調査する対象の URL の一覧
    * @param threshold しきい値とするはてなブックマーク件数
    * @return 絞り込んだ後の URL の一覧
    */
  def filter(urlList: Seq[String], threshold: Int): Seq[String] = {
    urlList.filter(url => {
      val starCount = fetchStarCount(url)
      starCount > threshold
    })
  }

  /**
    * 対象の URL のはてなブックマーク件数を取得する
    *
    * @param url 調査する対象の URL
    * @return 対象の URL のはてなブックマーク件数
    */
  def fetchStarCount(url: String): Int = {
    val request = sttp.get(uri"http://api.b.st-hatena.com/entry.count?${Map("url" -> url)}")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    response.body.getOrElse("0").toInt
  }
}
