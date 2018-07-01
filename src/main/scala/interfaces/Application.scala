package interfaces

import java.util.Date

import infrastructure.Settings.settings

object Application extends App {
  val executedAt = new Date
  val lastExecutedAt = Recorder.getLastExecutedAt

  val feedUrlList = Feeder.fetchUrlList(settings.feedUrl, settings.threshold, lastExecutedAt)
  Slack.post(settings.slackToken, settings.slackPostChannelId, settings.slackUserName, settings.slackIconEmoji, feedUrlList)

  Recorder.record(executedAt)
}
