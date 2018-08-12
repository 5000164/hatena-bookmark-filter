package infrastructure

import java.sql.Timestamp
import java.util.Date

import com.typesafe.scalalogging.LazyLogging
import infrastructure.Tables.{Articles, ArticlesRow}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random
import scala.util.control.NonFatal

/** DB に関する処理を行う。 */
class Repository extends LazyLogging {
  val db = Database.forURL("jdbc:h2:./db", driver = "org.h2.Driver")

  /** DB との接続を閉じる。
    */
  def close(): Unit = {
    db.close()
  }

  /** まだ処理していない記事を取得する。
    *
    * @return まだ処理していない記事の一覧
    */
  def fetchAllUnprocessed(): Seq[(String, Byte, Date)] = {
    val articles = TableQuery[Articles]
    val query = articles.filter(_.processed === false).map(t => (t.url, t.settingsId, t.createdAt))
    val result = Await.result(db.run(query.result), Duration.Inf)
    result.map(r => (r._1, r._2, new Date(r._3.getTime)))
  }

  /** 該当の設定に URL がすでに存在するか判断する。
    *
    * @param url        検索する URL
    * @param settingsId ウォッチの設定を行っている ID
    * @return 存在したかどうか
    */
  def existsUrl(url: String, settingsId: Byte): Boolean = {
    val articles = TableQuery[Articles]
    val q = articles.filter(r => (r.url === url) && (r.settingsId === settingsId)).exists
    val action = q.result
    val result = db.run(action)
    Await.result(result, Duration.Inf)
  }

  /** URL を保存する。
    *
    * @param url        保存する URL
    * @param settingsId URL に紐づく設定 ID
    * @return 実行結果
    */
  def save(url: String, settingsId: Byte): Either[Throwable, Unit] = {
    val articles = TableQuery[Articles]
    val date = new java.util.Date()
    val insertActions = DBIO.seq(
      articles += ArticlesRow(0, url, settingsId, false, new Timestamp(date.getTime), new Timestamp(date.getTime))
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

  /** 処理した記事を処理済みとしてマークする。
    *
    * @param url        すでに処理した記事の URL
    * @param settingsId すでに処理した記事の設定 ID
    * @return 実行結果
    */
  def processed(url: String, settingsId: Byte): Either[Throwable, Unit] = {
    val articles = TableQuery[Articles]
    val date = new java.util.Date()
    val q = for {a <- articles if a.url === url && a.settingsId === settingsId} yield (a.processed, a.updatedAt)
    val updateAction = q.update(true, new Timestamp(date.getTime))

    try {
      Right(Await.result(db.run(updateAction), Duration.Inf))
    } catch {
      case NonFatal(e1) =>
        logger.info(s"保存失敗 1 回目 $url", e1)
        Thread.sleep((Random.nextInt(91) + 10) * 100)
        try {
          Right(Await.result(db.run(updateAction), Duration.Inf))
        } catch {
          case NonFatal(e2) =>
            logger.info(s"保存失敗 2 回目 $url", e2)
            Thread.sleep((Random.nextInt(91) + 10) * 100)
            try {
              Right(Await.result(db.run(updateAction), Duration.Inf))
            } catch {
              case NonFatal(e3) =>
                logger.info(s"保存失敗 3 回目 $url", e3)
                Thread.sleep((Random.nextInt(91) + 10) * 100)
                try {
                  Right(Await.result(db.run(updateAction), Duration.Inf))
                } catch {
                  case NonFatal(e4) =>
                    logger.info(s"保存失敗 4 回目 $url", e4)
                    Thread.sleep((Random.nextInt(91) + 10) * 100)
                    try {
                      Right(Await.result(db.run(updateAction), Duration.Inf))
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
}
