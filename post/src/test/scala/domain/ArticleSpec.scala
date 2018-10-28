package domain

import java.time.LocalDateTime

import domain.Article.judge
import org.scalatest.FeatureSpec

class ArticleSpec extends FeatureSpec {
  feature("Slack に投稿するために変換できる") {
    scenario("オブジェクトを変換する") {
      assert(
        Article(
          url = "https://blog.5000164.jp/",
          title = "5000164 is here",
          bookmarkCount = 0,
          postChannelId = "postChannelId",
          userName = "userName",
          iconEmoji = "iconEmoji"
        ).toSlackString ===
          """:bookmark: 0
            |5000164 is here
            |https://blog.5000164.jp/
            |http://b.hatena.ne.jp/entry/s/blog.5000164.jp/""".stripMargin
      )
    }
  }

  feature("URL の状態に応じて結果を判定することができる") {
    scenario("指定した時間を経過していない場合は Still になる") {
      assert(
        judge(
          url = "",
          now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
          createdAt = LocalDateTime.of(2017, 12, 31, 23, 59, 59),
          waitSeconds = 1,
          fetchBookmarkCount = _ => 0,
          threshold = 0
        ) === (Still, None)
      )
    }

    scenario("指定した時間を経過していてブックマーク数がしきい値を下回っている場合は NotQualified になる") {
      assert(
        judge(
          url = "",
          now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
          createdAt = LocalDateTime.of(2017, 12, 31, 23, 59, 59),
          waitSeconds = 0,
          fetchBookmarkCount = _ => 1,
          threshold = 2
        ) === (NotQualified, None)
      )
    }

    scenario("指定した時間を経過していてブックマーク数がしきい値と同じ場合は Qualified になる") {
      assert(
        judge(
          url = "",
          now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
          createdAt = LocalDateTime.of(2017, 12, 31, 23, 59, 59),
          waitSeconds = 0,
          fetchBookmarkCount = _ => 2,
          threshold = 2
        ) === (Qualified, Some(2))
      )
    }

    scenario("指定した時間を経過していてブックマーク数がしきい値を上回っている場合は Qualified になる") {
      assert(
        judge(
          url = "",
          now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
          createdAt = LocalDateTime.of(2017, 12, 31, 23, 59, 59),
          waitSeconds = 0,
          fetchBookmarkCount = _ => 3,
          threshold = 2
        ) === (Qualified, Some(3))
      )
    }
  }
}
