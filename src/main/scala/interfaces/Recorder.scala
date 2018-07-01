package interfaces

import java.io.PrintWriter
import java.nio.file.{Files, Paths}
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

import scala.io.Source

/**
  * 実行時間の記録に関する処理を行う。
  */
object Recorder {
  /**
    * 最終実行時間を取得する。
    *
    * @return 取得した最終実行時間
    */
  def getLastExecutedAt: Option[Date] = {
    if (Files.exists(Paths.get(".record"))) {
      val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"))

      val record = Source.fromFile(".record").mkString
      Some(sdf.parse(record))
    } else {
      None
    }
  }

  /**
    * 最終実行時間を記録する。
    *
    * @param executedAt 記録する実行時間
    */
  def record(executedAt: Date): Unit = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
    val record = sdf.format(executedAt)
    if (Files.notExists(Paths.get(".record"))) Files.createFile(Paths.get(".record"))
    val pw = new PrintWriter(".record")
    pw.write(record)
    pw.close()
  }
}
