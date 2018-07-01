package interfaces

import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

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
    * @param feedUrl        ページを取得する元の RSS の URL
    * @param threshold      しきい値とするはてなブックマークの件数
    * @param lastExecutedAt 更新されたページのみを取得するため使用する最終実行日時
    * @return 条件を満たした URL の一覧
    */
  def fetchUrlList(feedUrl: String, threshold: Int, lastExecutedAt: Option[Date]): Seq[String] = {
    val deliveredPageList = fetchDeliveredPageList(feedUrl)
    filter(deliveredPageList, threshold, lastExecutedAt)
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
    * @param pageList       調査する対象のページ一覧
    * @param threshold      しきい値とするはてなブックマーク件数
    * @param lastExecutedAt 更新されたページのみを取得するため使用する最終実行日時
    * @return 絞り込んだ後の URL の一覧
    */
  def filter(pageList: Seq[Page], threshold: Int, lastExecutedAt: Option[Date]): Seq[String] =
    pageList.filter(page => {
      val starCount = fetchStarCount(page.url)
      starCount > threshold
    }).filter(page => {
      lastExecutedAt match {
        case Some(pointDate) =>
          val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
          sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
          val pageDate = sdf.parse(page.date)
          pageDate.after(pointDate)
        case None => true
      }
    }).map(_.url)

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
