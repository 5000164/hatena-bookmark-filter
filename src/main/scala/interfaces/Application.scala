package interfaces

import infrastructure.Settings.settings

object Application extends App {
  val pageList = Feeder.fetchPageList(settings.watches)
  Slack.post(settings.slackToken, pageList)
}
