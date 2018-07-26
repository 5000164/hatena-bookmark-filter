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
    * @param db  接続先の DB
    * @param url 保存する URL
    * @return 実行結果
    */
  def save(db: DatabaseDef, url: String): Either[Throwable, Unit] = {
    val articles = TableQuery[Articles]
    val insertActions = DBIO.seq(
      articles += ArticlesRow(None, url, 1, java.time.ZonedDateTime.now.toEpochSecond.toInt, java.time.ZonedDateTime.now.toEpochSecond.toInt)
    )
    try {
      Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
    } catch {
      case NonFatal(e1) =>
        logger.info("1 度目の失敗: " + e1.toString)
        Thread.sleep((Random.nextInt(91) + 10) * 100)
        try {
          Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
        } catch {
          case NonFatal(e2) =>
            logger.info("2 度目の失敗: " + e2.toString)
            Thread.sleep((Random.nextInt(91) + 10) * 100)
            try {
              Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
            } catch {
              case NonFatal(e3) =>
                logger.info("3 度目の失敗: " + e3.toString)
                Thread.sleep((Random.nextInt(91) + 10) * 100)
                try {
                  Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
                } catch {
                  case NonFatal(e4) =>
                    logger.info("4 度目の失敗: " + e4.toString)
                    Thread.sleep((Random.nextInt(91) + 10) * 100)
                    try {
                      Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
                    } catch {
                      case NonFatal(e5) =>
                        logger.error("5 度目の失敗: " + e5.toString)
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
