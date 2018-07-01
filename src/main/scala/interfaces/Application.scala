package interfaces

import infrastructure.Settings.settings

object Application extends App {
  val feedUrlList = Feeder.fetchUrlList(settings.feedUrl, settings.threshold)
  Slack.post(settings.slackToken, settings.slackPostChannelId, settings.slackUserName, settings.slackIconEmoji, feedUrlList)
}
