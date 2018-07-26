package domain

import domain.Judge.refine
import org.scalatest.FeatureSpec

class JudgeSpec extends FeatureSpec {
  feature("URL を絞り込むことができる") {
    scenario("すでに投稿済みの場合は None になる") {
      assert(refine(
        url = "",
        threshold = 0,
        existsUrl = _ => true,
        fetchBookmarkCount = _ => 0) === None)
    }

    scenario("ブックマーク数がしきい値を下回っている場合は None になる") {
      assert(refine(
        url = "",
        threshold = 2,
        existsUrl = _ => false,
        fetchBookmarkCount = _ => 1) === None)
    }

    scenario("ブックマーク数がしきい値と同じ場合は Some になる") {
      assert(refine(
        url = "",
        threshold = 2,
        existsUrl = _ => false,
        fetchBookmarkCount = _ => 2) === Some(2))
    }

    scenario("ブックマーク数がしきい値を上回っている場合は Some になる") {
      assert(refine(
        url = "",
        threshold = 2,
        existsUrl = _ => false,
        fetchBookmarkCount = _ => 3) === Some(3))
    }
  }
}
