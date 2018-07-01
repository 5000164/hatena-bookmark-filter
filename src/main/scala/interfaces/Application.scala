package interfaces

import java.util.Date

import infrastructure.Settings.settings

object Application extends App {
  val executedAt = new Date
  val lastExecutedAt = Recorder.getLastExecutedAt

  val pageList = Feeder.fetchPageList(settings.feedUrl, settings.threshold, lastExecutedAt)
  Slack.post(settings.slackToken, settings.slackPostChannelId, settings.slackUserName, settings.slackIconEmoji, pageList)

  Recorder.record(executedAt)
}
