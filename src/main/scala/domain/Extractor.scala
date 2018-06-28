package domain

/**
  * 抽出する処理を行う
  */
object Extractor {
  /**
    * URL を抽出する
    *
    * @param raw 対象の文字列
    * @return 抽出結果
    */
  def extractUrl(raw: String): Either[Unit, String] = {
    val r = """.*rdf:resource="(.*?)".*""".r
    r.findFirstMatchIn(raw) match {
      case Some(matches) => Right(matches.group(1))
      case None => Left(())
    }
  }
}
