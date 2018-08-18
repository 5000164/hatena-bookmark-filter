package infrastructure

import scala.io.Source
import scala.reflect.runtime.{currentMirror, universe}
import scala.tools.reflect.ToolBox

object Settings {
  val toolbox: ToolBox[universe.type] = currentMirror.mkToolBox()
  val settings: SettingsType = toolbox.eval(toolbox.parse(Source.fromFile(System.getProperty("settings")).mkString)).asInstanceOf[SettingsType]
}

trait SettingsType {
  /** ウォッチ設定を記述する */
  val watches: Map[Byte, WatchSettings]

  /** Slack に投稿するために Bot の API Token を設定する */
  val slackToken: String

  /** 投稿処理を行う時の並列数を指定する */
  val parallelPostCount: Int
}

case class WatchSettings(
    /** ウォッチするはてなブックマークのフィード URL を指定する */
    feedUrl: String,

    /** Slack に投稿するしきい値となるブックマーク数を指定する */
    threshold: Int,

    /** 記事を収集してから投稿までの待ち時間を指定する (評価が安定してからブックマーク数のしきい値で判定することができるようになる) */
    waitSeconds: Int,

    /** Slack にどのように投稿するかを設定する */
    slack: SlackSettings)

case class SlackSettings(
    /** 記事を投稿するチャンネルを指定する */
    postChannelId: String,

    /** 投稿ユーザー表示名を指定する */
    userName: String,

    /** 投稿ユーザーアイコンを指定する */
    iconEmoji: String)
