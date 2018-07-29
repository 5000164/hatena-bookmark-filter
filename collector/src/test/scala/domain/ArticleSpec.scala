package domain

import org.scalatest.FeatureSpec

class ArticleSpec extends FeatureSpec {
  feature("Slack に投稿するために変換できる") {
    scenario("オブジェクトを変換する") {
      assert(Article(
        url = "https://blog.5000164.jp/",
        title = "5000164 is here",
        bookmarkCount = 0,
        postChannelId = "postChannelId",
        userName = "userName",
        iconEmoji = "iconEmoji").toSlackString ===
        """0 :bookmark:
          |5000164 is here
          |https://blog.5000164.jp/
          |http://b.hatena.ne.jp/entry/s/blog.5000164.jp/""".stripMargin)
    }
  }
}
