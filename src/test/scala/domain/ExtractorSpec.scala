package domain

import domain.Extractor.extractUrl
import org.scalatest.FeatureSpec

class ExtractorSpec extends FeatureSpec {
  feature("URL を抽出できる") {
    scenario("RSS の要素から URL を抽出する") {
      val raw = """<rdf:li rdf:resource="https://blog.5000164.jp/" xmlns:taxo="http://purl.org/rss/1.0/modules/taxonomy/" xmlns:syn="http://purl.org/rss/1.0/modules/syndication/" xmlns:hatena="http://www.hatena.ne.jp/info/xmlns#" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:admin="http://webns.net/mvcb/" xmlns="http://purl.org/rss/1.0/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"/>"""
      assert(extractUrl(raw) === Right("https://blog.5000164.jp/"))
    }
  }
}
