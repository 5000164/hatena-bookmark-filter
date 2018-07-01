import infrastructure.{SettingsType, SlackSettings, WatchSettings}

new SettingsType {
  override val watches: Seq[WatchSettings] = Seq(
    WatchSettings(
      feedUrl = "http://b.hatena.ne.jp/hotentry/all.rss",
      threshold = 500,
      slack = SlackSettings(
        postChannelId = "#channel_id",
        userName = "USER NAME",
        iconEmoji = ":+1:")
    ),
    WatchSettings(
      feedUrl = "http://b.hatena.ne.jp/hotentry/it.rss",
      threshold = 100,
      slack = SlackSettings(
        postChannelId = "#channel_id",
        userName = "USER NAME",
        iconEmoji = ":+1:")
    ))
  override val slackToken: String = "SLACK_TOKEN"
}
