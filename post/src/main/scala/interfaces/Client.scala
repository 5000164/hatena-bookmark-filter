package interfaces

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

/** 通信に関する処理を行う。 */
object Client {
  /** 対象 URL のタイトルを取得する
    *
    * @param url 取得する対象の URL
    * @return 対象 URL のタイトル
    */
  def fetchTitle(url: String): String = {
    val browser = JsoupBrowser()
    val doc = browser.get(url)
    doc >> text("title")
  }
}
