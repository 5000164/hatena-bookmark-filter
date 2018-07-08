package domain

/**
  * 判断を行う。
  */
object Judge {
  /**
    * 配信された記事から投稿する記事だけを絞り込む。
    *
    * @param url                配信された URL
    * @param threshold          ブックマーク数のしきい値とする値
    * @param existsUrl          対象の URL がすでに投稿済みか判断する処理
    * @param fetchBookmarkCount 対象の URL のブックマーク数を取得する処理
    * @return 絞り込んだ結果として、投稿するものは Some、投稿しないものは None を返す、Some の中にはブックマーク数の取得処理を何回も行わないために取得したブックマーク数を返す
    */
  def refine(url: String, threshold: Int, existsUrl: String => Boolean, fetchBookmarkCount: String => Int): Option[Int] = {
    if (existsUrl(url)) None
    else {
      val bookmarkCount = fetchBookmarkCount(url)
      if (bookmarkCount >= threshold) Some(bookmarkCount) else None
    }
  }
}
