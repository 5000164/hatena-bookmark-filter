package interfaces

import com.softwaremill.sttp._

/** 通信に関する処理を行う。 */
object Client {
  /** 指定された URL の内容を返す。
    *
    * @param url 取得対象の URL
    * @return 取得した内容
    */
  def fetchContent(url: String): String = {
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    sttp.get(uri"$url").send().body.getOrElse("")
  }
}
