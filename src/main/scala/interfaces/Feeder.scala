package interfaces

import com.softwaremill.sttp._
import domain.Extractor.extractPage
import domain.Page

/**
  * RSS に関する処理を行う
  */
object Feeder {
  /**
    * 条件を満たした URL の一覧を取得する
    *
    * @return 条件を満たした URL の一覧
    */
  def fetchUrlList(feedUrl: String, threshold: Int): Seq[String] = {
    val deliveredPageList = fetchDeliveredPageList(feedUrl)
    filter(deliveredPageList, threshold)
  }

  /**
    * 配信されたページの一覧を取得する
    *
    * @return 配信されたページの一覧
    */
  def fetchDeliveredPageList(feedUrl: String): Seq[Page] = {
    val request = sttp.get(uri"$feedUrl")
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val xmlString = response.body.getOrElse("")
    extractPage(xmlString)
  }

  /**
    * 条件を元に絞り込む
    *
    * @param pageList  調査する対象のページ一覧
    * @param threshold しきい値とするはてなブックマーク件数
    * @return 絞り込んだ後の URL の一覧
    */
  def filter(pageList: Seq[Page], threshold: Int): Seq[String] = {
    pageList.filter(page => {
      val starCount = fetchStarCount(page.url)
      starCount > threshold
    }).map(_.url)
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
