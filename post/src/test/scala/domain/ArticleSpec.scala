package domain

import java.time.LocalDateTime

import domain.Article.refine
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
        """:bookmark: 0
          |5000164 is here
          |https://blog.5000164.jp/
          |http://b.hatena.ne.jp/entry/s/blog.5000164.jp/""".stripMargin)
    }
  }

  feature("URL を絞り込むことができる") {
    scenario("指定した時間を経過していない場合は None になる") {
      assert(refine(
        url = "",
        now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        createdAt = LocalDateTime.of(2017, 12, 31, 23, 59, 59),
        waitSeconds = 1,
        fetchBookmarkCount = _ => 0,
        threshold = 0) === None)
    }

    scenario("指定した時間を経過している場合は Some になる") {
      assert(refine(
        url = "",
        now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        createdAt = LocalDateTime.of(2017, 12, 31, 23, 59, 59),
        waitSeconds = 0,
        fetchBookmarkCount = _ => 0,
        threshold = 0) === Some(0))
    }

    scenario("ブックマーク数がしきい値を下回っている場合は None になる") {
      assert(refine(
        url = "",
        now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        createdAt = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        waitSeconds = 0,
        fetchBookmarkCount = _ => 1,
        threshold = 2) === None)
    }

    scenario("ブックマーク数がしきい値と同じ場合は Some になる") {
      assert(refine(
        url = "",
        now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        createdAt = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        waitSeconds = 0,
        fetchBookmarkCount = _ => 2,
        threshold = 2) === None)
    }

    scenario("ブックマーク数がしきい値を上回っている場合は Some になる") {
      assert(refine(
        url = "",
        now = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        createdAt = LocalDateTime.of(2018, 1, 1, 0, 0, 0),
        waitSeconds = 0,
        fetchBookmarkCount = _ => 3,
        threshold = 2) === None)
    }
  }
}
