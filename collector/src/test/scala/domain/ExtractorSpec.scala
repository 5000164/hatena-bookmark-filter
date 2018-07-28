package domain

import domain.Extractor.fetchDeliveredArticles
import org.scalatest.FeatureSpec

import scala.io.Source

class ExtractorSpec extends FeatureSpec {
  feature("記事を抽出できる") {
    scenario("XML から記事を抽出する") {
      assert(fetchDeliveredArticles(
        feedUrl = "feedUrl",
        fetchContent = _ => Source.fromResource("sample.rss").getLines.mkString) === Seq(
        DeliveredArticle("http://gendai.ismedia.jp/articles/-/56258"),
        DeliveredArticle("http://hosyusokuhou.jp/archives/48819730.html"),
        DeliveredArticle("https://togetter.com/li/1241708"),
        DeliveredArticle("https://www3.nhk.or.jp/news/html/20180701/k10011503371000.html"),
        DeliveredArticle("https://togetter.com/li/1242324"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701081730"),
        DeliveredArticle("https://www3.nhk.or.jp/news/html/20180701/k10011503121000.html"),
        DeliveredArticle("https://anond.hatelabo.jp/20180624140926"),
        DeliveredArticle("https://natalie.mu/owarai/news/289169"),
        DeliveredArticle("https://note.mu/psychs/n/nc519c2c85801"),
        DeliveredArticle("http://twitter.com/lullymiura/status/1013022237962588161"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701133232"),
        DeliveredArticle("http://novtan.hatenablog.com/entry/2018/07/01/103710"),
        DeliveredArticle("http://bunshun.jp/articles/-/7957"),
        DeliveredArticle("https://togetter.com/li/1242449"),
        DeliveredArticle("http://kabumatome.doorblog.jp/archives/65921871.html"),
        DeliveredArticle("https://anond.hatelabo.jp/20180629224012"),
        DeliveredArticle("https://www3.nhk.or.jp/news/html/20180701/k10011503031000.html"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701121356"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701010648"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701155712"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701063754"),
        DeliveredArticle("https://togetter.com/li/1242309"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701012637"),
        DeliveredArticle("https://mainichi.jp/articles/20180701/k00/00m/040/093000c"),
        DeliveredArticle("http://blog.inouetakuya.info/entry/2018/06/30/204134"),
        DeliveredArticle("https://note.mu/psychs/n/nf073d58a2c4e"),
        DeliveredArticle("https://okane.news/financial-movies/"),
        DeliveredArticle("https://samurai20.jp/2018/06/hosyusoku-2/"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701000252")))
    }
  }
}
