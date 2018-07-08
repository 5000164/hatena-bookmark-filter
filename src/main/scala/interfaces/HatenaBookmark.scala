package interfaces

import com.softwaremill.sttp._

/**
  * はてなブックマークに関する処理を行う。
  */
object HatenaBookmark {
  /**
    * 対象の URL のはてなブックマーク件数を取得する。
    *
    * @param url 調査する対象の URL
    * @return 対象の URL のはてなブックマーク件数
    */
  def fetchBookmarkCount(url: String): Int = {
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    sttp.get(uri"http://api.b.st-hatena.com/entry.count?${Map("url" -> url)}").send().body.getOrElse("0").toInt
  }

  /**
    * はてなブックマークコメントページの URL を構築する。
    *
    * @param url この URL へのコメントページの URL を構築する
    * @return はてなブックマークコメントページの URL
    */
  def buildCommentUrl(url: String): String = {
    val connector = if (url(4) == 's') {"s/"} else {""}
    val path = url.split(':')(1).drop(2)
    s"http://b.hatena.ne.jp/entry/$connector$path"
  }
}
