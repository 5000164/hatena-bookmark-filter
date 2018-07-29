package interfaces

import com.typesafe.scalalogging.LazyLogging
import infrastructure.Tables.{Articles, ArticlesRow}
import slick.jdbc.SQLiteProfile.api._
import slick.jdbc.SQLiteProfile.backend.DatabaseDef

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random
import scala.util.control.NonFatal

/** DB に関する処理を行う。 */
object Repository extends LazyLogging {
  /** URL を保存する。
    *
    * @param db         接続先の DB
    * @param url        保存する URL
    * @param settingsId URL に紐づく設定 ID
    * @return 実行結果
    */
  def save(db: DatabaseDef, url: String, settingsId: Int): Either[Throwable, Unit] = {
    val articles = TableQuery[Articles]
    val insertActions = DBIO.seq(
      articles += ArticlesRow(None, url, settingsId, 1, java.time.ZonedDateTime.now.toEpochSecond.toInt, java.time.ZonedDateTime.now.toEpochSecond.toInt)
    )
    try {
      Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
    } catch {
      case NonFatal(e1) =>
        logger.info(s"保存失敗 1 回目 $url", e1)
        Thread.sleep((Random.nextInt(91) + 10) * 100)
        try {
          Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
        } catch {
          case NonFatal(e2) =>
            logger.info(s"保存失敗 2 回目 $url", e2)
            Thread.sleep((Random.nextInt(91) + 10) * 100)
            try {
              Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
            } catch {
              case NonFatal(e3) =>
                logger.info(s"保存失敗 3 回目 $url", e3)
                Thread.sleep((Random.nextInt(91) + 10) * 100)
                try {
                  Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
                } catch {
                  case NonFatal(e4) =>
                    logger.info(s"保存失敗 4 回目 $url", e4)
                    Thread.sleep((Random.nextInt(91) + 10) * 100)
                    try {
                      Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
                    } catch {
                      case NonFatal(e5) =>
                        logger.info(s"保存失敗 5 回目 $url", e5)
                        Left(e5)
                    }
                }
            }
        }
    }
  }

  /** URL がすでに存在するか判断する。
    *
    * @param db  接続先の DB
    * @param url 検索する URL
    * @return 存在したかどうか
    */
  def existsUrl(db: DatabaseDef, url: String): Boolean = {
    val articles = TableQuery[Articles]
    val q = articles.filter(_.url === url).exists
    val action = q.result
    val result = db.run(action)
    Await.result(result, Duration.Inf)
  }
}
