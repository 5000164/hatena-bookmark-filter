package domain

import org.scalatest.FeatureSpec

class ArticleSpec extends FeatureSpec {
  feature("Slack に投稿するために変換できる") {
    scenario("オブジェクトを変換する") {
      assert(Article(
        url = "https://blog.5000164.jp/",
        bookmarkCount = 0,
        postChannelId = "postChannelId",
        userName = "userName",
        iconEmoji = "iconEmoji").toSlackString ===
        """はてなブックマーク数: 0
          |https://blog.5000164.jp/
          |http://b.hatena.ne.jp/entry/s/blog.5000164.jp/""".stripMargin)
    }
  }
}
