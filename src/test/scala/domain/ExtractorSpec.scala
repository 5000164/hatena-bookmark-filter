package domain

import domain.Extractor.extractUrl
import org.scalatest.FeatureSpec

import scala.io.Source

class ExtractorSpec extends FeatureSpec {
  feature("URL を抽出できる") {
    scenario("RSS から URL を抽出する") {
      val rss = Source.fromResource("sample.rss").getLines.mkString
      assert(extractUrl(rss) === Seq(
        "http://gendai.ismedia.jp/articles/-/56258",
        "http://hosyusokuhou.jp/archives/48819730.html",
        "https://togetter.com/li/1241708",
        "https://www3.nhk.or.jp/news/html/20180701/k10011503371000.html",
        "https://togetter.com/li/1242324",
        "https://anond.hatelabo.jp/20180701081730",
        "https://www3.nhk.or.jp/news/html/20180701/k10011503121000.html",
        "https://anond.hatelabo.jp/20180624140926",
        "https://natalie.mu/owarai/news/289169",
        "https://note.mu/psychs/n/nc519c2c85801",
        "http://twitter.com/lullymiura/status/1013022237962588161",
        "https://anond.hatelabo.jp/20180701133232",
        "http://novtan.hatenablog.com/entry/2018/07/01/103710",
        "http://bunshun.jp/articles/-/7957",
        "https://togetter.com/li/1242449",
        "http://kabumatome.doorblog.jp/archives/65921871.html",
        "https://anond.hatelabo.jp/20180629224012",
        "https://www3.nhk.or.jp/news/html/20180701/k10011503031000.html",
        "https://anond.hatelabo.jp/20180701121356",
        "https://anond.hatelabo.jp/20180701010648",
        "https://anond.hatelabo.jp/20180701155712",
        "https://anond.hatelabo.jp/20180701063754",
        "https://togetter.com/li/1242309",
        "https://anond.hatelabo.jp/20180701012637",
        "https://mainichi.jp/articles/20180701/k00/00m/040/093000c",
        "http://blog.inouetakuya.info/entry/2018/06/30/204134",
        "https://note.mu/psychs/n/nf073d58a2c4e",
        "https://okane.news/financial-movies/",
        "https://samurai20.jp/2018/06/hosyusoku-2/",
        "https://anond.hatelabo.jp/20180701000252"))
    }
  }
}
