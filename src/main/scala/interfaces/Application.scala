package interfaces

import infrastructure.Settings.settings

object Application extends App {
  val articleList = Feeder.fetchArticleList(settings.watches)
  Slack.post(settings.slackToken, articleList)
}
