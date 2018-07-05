package interfaces

import java.util.Date

import infrastructure.Settings.settings

object Application extends App {
  val executedAt = new Date
  val lastExecutedAt = Recorder.getLastExecutedAt

  val pageList = Feeder.fetchPageList(settings.watches, lastExecutedAt)
  Slack.post(settings.slackToken, pageList)

  Recorder.record(executedAt)
}
