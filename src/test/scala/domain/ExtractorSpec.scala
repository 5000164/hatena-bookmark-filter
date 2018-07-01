package domain

import domain.Extractor.extractPage
import org.scalatest.FeatureSpec

import scala.io.Source

class ExtractorSpec extends FeatureSpec {
  feature("URL を抽出できる") {
    scenario("RSS から URL を抽出する") {
      val rss = Source.fromResource("sample.rss").getLines.mkString
      assert(extractPage(rss) === Seq(
        DeliveredPage("http://gendai.ismedia.jp/articles/-/56258", "2018-07-01T01:26:51Z"),
        DeliveredPage("http://hosyusokuhou.jp/archives/48819730.html", "2018-07-01T04:27:25Z"),
        DeliveredPage("https://togetter.com/li/1241708", "2018-06-30T19:23:26Z"),
        DeliveredPage("https://www3.nhk.or.jp/news/html/20180701/k10011503371000.html", "2018-07-01T02:51:37Z"),
        DeliveredPage("https://togetter.com/li/1242324", "2018-06-30T22:07:12Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701081730", "2018-07-01T04:35:52Z"),
        DeliveredPage("https://www3.nhk.or.jp/news/html/20180701/k10011503121000.html", "2018-06-30T20:01:40Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180624140926", "2018-06-30T10:18:57Z"),
        DeliveredPage("https://natalie.mu/owarai/news/289169", "2018-06-30T21:09:34Z"),
        DeliveredPage("https://note.mu/psychs/n/nc519c2c85801", "2018-07-01T00:40:31Z"),
        DeliveredPage("http://twitter.com/lullymiura/status/1013022237962588161", "2018-06-30T19:16:28Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701133232", "2018-07-01T04:44:45Z"),
        DeliveredPage("http://novtan.hatenablog.com/entry/2018/07/01/103710", "2018-07-01T01:38:32Z"),
        DeliveredPage("http://bunshun.jp/articles/-/7957", "2018-06-30T22:19:59Z"),
        DeliveredPage("https://togetter.com/li/1242449", "2018-06-30T22:19:09Z"),
        DeliveredPage("http://kabumatome.doorblog.jp/archives/65921871.html", "2018-06-30T17:27:47Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180629224012", "2018-06-30T12:38:04Z"),
        DeliveredPage("https://www3.nhk.or.jp/news/html/20180701/k10011503031000.html", "2018-06-30T23:14:40Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701121356", "2018-07-01T03:47:07Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701010648", "2018-06-30T21:54:58Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701155712", "2018-07-01T07:09:38Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701063754", "2018-06-30T21:51:55Z"),
        DeliveredPage("https://togetter.com/li/1242309", "2018-06-30T13:27:02Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701012637", "2018-06-30T16:54:06Z"),
        DeliveredPage("https://mainichi.jp/articles/20180701/k00/00m/040/093000c", "2018-07-01T01:06:54Z"),
        DeliveredPage("http://blog.inouetakuya.info/entry/2018/06/30/204134", "2018-06-30T12:56:56Z"),
        DeliveredPage("https://note.mu/psychs/n/nf073d58a2c4e", "2018-07-01T06:38:31Z"),
        DeliveredPage("https://okane.news/financial-movies/", "2018-07-01T03:04:43Z"),
        DeliveredPage("https://samurai20.jp/2018/06/hosyusoku-2/", "2018-06-30T21:59:52Z"),
        DeliveredPage("https://anond.hatelabo.jp/20180701000252", "2018-06-30T22:13:16Z")))
    }
  }
}
