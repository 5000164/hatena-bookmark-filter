package infrastructure

import scala.io.Source
import scala.reflect.runtime.{currentMirror, universe}
import scala.tools.reflect.ToolBox

object Settings {
  val toolbox: ToolBox[universe.type] = currentMirror.mkToolBox()
  val settings: SettingsType = toolbox.eval(toolbox.parse(Source.fromResource("Settings.scala").mkString)).asInstanceOf[SettingsType]
}

trait SettingsType {
  val feedUrl: String
  val threshold: Int
  val slackToken: String
  val slackPostChannelId: String
  val slackUserName: String
  val slackIconEmoji: String
}
