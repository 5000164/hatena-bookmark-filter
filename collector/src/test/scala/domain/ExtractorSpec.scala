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
        DeliveredArticle("http://gendai.ismedia.jp/articles/-/56258", "凶悪犯罪続発！アメリカを蝕む「非モテの過激化」という大問題（八田 真行） | 現代ビジネス | 講談社（1/4）"),
        DeliveredArticle("http://hosyusokuhou.jp/archives/48819730.html", "保守速報からのお知らせ ｜ 保守速報"),
        DeliveredArticle("https://togetter.com/li/1241708", "電車で見たお姉さんを描いたイラストが性的すぎて炎上 - Togetter"),
        DeliveredArticle("https://www3.nhk.or.jp/news/html/20180701/k10011503371000.html", "13歳が車運転し事故 １人死亡 ４人大けが 岡山 | NHKニュース"),
        DeliveredArticle("https://togetter.com/li/1242324", "入社2年目の若手エンジニアがものすごく優秀だったので上司が『彼を課長待遇に』と提案するも即座に却下されてしまうという日本企業の“常識的”マネージメントについての話 - Togetter"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701081730", "こういうブコメの書き方やめろ"),
        DeliveredArticle("https://www3.nhk.or.jp/news/html/20180701/k10011503121000.html", "かぜに効かない抗菌薬 ６割超の医師が処方 | NHKニュース"),
        DeliveredArticle("https://anond.hatelabo.jp/20180624140926", "外に出ろって言うけど何をすればいいの？"),
        DeliveredArticle("https://natalie.mu/owarai/news/289169", "明石家さんま、“勘違い”で34年ぶりテレ東出演！出川哲朗と山形へ電動バイク旅（コメントあり） - お笑いナタリー"),
        DeliveredArticle("https://note.mu/psychs/n/nc519c2c85801", "Twitter での6年間 1｜Satoshi Nakagawa｜note"),
        DeliveredArticle("http://twitter.com/lullymiura/status/1013022237962588161", "三浦瑠麗 Lully MIURAさんのツイート: \"杉田水脈議員が詩織さんの件に落ち度云々とコメントしていますが、仮に財布がズボンのポケットからはみ出て気をつけてないうちにスられたとしても、"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701133232", "今年外科には誰も入局者がいなかった"),
        DeliveredArticle("http://novtan.hatenablog.com/entry/2018/07/01/103710", "われわれは反省すべきか、萎縮すべきか、Hagexの遺志を継ぐべきか - novtanの日常"),
        DeliveredArticle("http://bunshun.jp/articles/-/7957", "「正社員はいらない」“煽る人”竹中平蔵とは何者なのか？ | 文春オンライン"),
        DeliveredArticle("https://togetter.com/li/1242449", "「怪物と戦っていると思ったら、その正体は人間だった…」というお話の起源等を巡る話（※ネタバレは自己責任で） - Togetter"),
        DeliveredArticle("http://kabumatome.doorblog.jp/archives/65921871.html", "トヨタ自動車の内部留保、日本共産党と朝日新聞がナントカ埋蔵金の如く非難 : 市況かぶ全力２階建"),
        DeliveredArticle("https://anond.hatelabo.jp/20180629224012", "態度の悪い４０代無職を誰が救えるの？"),
        DeliveredArticle("https://www3.nhk.or.jp/news/html/20180701/k10011503031000.html", "経済的に厳しい家庭も保護者の関与で子どもの学力向上 | NHKニュース"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701121356", "ぶっちゃけ2人っきりの飲み食い奢られて"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701010648", "女医さんがやってきた"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701155712", "今日もブクマカは増田叩き"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701063754", "あいつが低能先生だった"),
        DeliveredArticle("https://togetter.com/li/1242309", "「私28歳。結婚してない恋もしてない。仕事もダメ。なにもない」朝ドラ『半分青い』のセリフが賛否両論　しかもこれの続きは男性を狙った二段構え - Togetter"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701012637", "酒が嫌いだというだけの愚痴"),
        DeliveredArticle("https://mainichi.jp/articles/20180701/k00/00m/040/093000c", "ＩＴ講師刺殺１週間：ネット憎悪がリアル暴力に - 毎日新聞"),
        DeliveredArticle("http://blog.inouetakuya.info/entry/2018/06/30/204134", "49インチ 4K ディスプレイの感想 - 彼女からは、おいちゃんと呼ばれています"),
        DeliveredArticle("https://note.mu/psychs/n/nf073d58a2c4e", "Twitter での6年間 2｜Satoshi Nakagawa｜note"),
        DeliveredArticle("https://okane.news/financial-movies/", "金融リテラシーが上がる！映画まとめ30選｜投資や経済を楽しく学べる | お得に節約お金ニュース"),
        DeliveredArticle("https://samurai20.jp/2018/06/hosyusoku-2/", "保守速報など、まとめサイト群への救済処置（暫定版）【応援する人はシェア】 | 小坪しんやのHP～行橋市議会議員"),
        DeliveredArticle("https://anond.hatelabo.jp/20180701000252", "たまごとうふには豆腐成分が一切入ってないと知った時の衝撃は忘れない")))
    }
  }
}
