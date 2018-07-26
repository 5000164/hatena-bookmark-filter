package interfaces

import infrastructure.Tables.{Articles, ArticlesRow}
import slick.jdbc.SQLiteProfile.api._
import slick.jdbc.SQLiteProfile.backend.DatabaseDef

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random

/** DB に関する処理を行う。 */
object Repository {
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
      case _: Throwable =>
        Thread.sleep((Random.nextInt(91) + 10) * 100)
        try {
          Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
        } catch {
          case _: Throwable =>
            Thread.sleep((Random.nextInt(91) + 10) * 100)
            try {
              Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
            } catch {
              case _: Throwable =>
                Thread.sleep((Random.nextInt(91) + 10) * 100)
                try {
                  Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
                } catch {
                  case _: Throwable =>
                    Thread.sleep((Random.nextInt(91) + 10) * 100)
                    try {
                      Right(Await.result(db.run(insertActions.transactionally), Duration.Inf))
                    } catch {
                      case e: Throwable => Left(e)
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
