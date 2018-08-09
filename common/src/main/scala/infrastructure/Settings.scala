package infrastructure

import scala.io.Source
import scala.reflect.runtime.{currentMirror, universe}
import scala.tools.reflect.ToolBox

object Settings {
  val toolbox: ToolBox[universe.type] = currentMirror.mkToolBox()
  val settings: SettingsType = toolbox.eval(toolbox.parse(Source.fromFile(System.getProperty("settings")).mkString)).asInstanceOf[SettingsType]
}

trait SettingsType {
  val watches: Map[Byte, WatchSettings]
  val slackToken: String
  val parallelPostCount: Int
}

case class WatchSettings(
    feedUrl: String,
    threshold: Int,
    slack: SlackSettings)

case class SlackSettings(
    postChannelId: String,
    userName: String,
    iconEmoji: String)
