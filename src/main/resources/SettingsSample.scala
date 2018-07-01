import infrastructure.SettingsType

new SettingsType {
  override val feedUrl: String = "http://b.hatena.ne.jp/hotentry/all.rss"
  override val threshold: Int = 500
  override val slackToken: String = "SLACK_TOKEN"
  override val slackPostChannelId: String = "#channel_id"
  override val slackUserName: String = "USER NAME"
  override val slackIconEmoji: String = ":+1:"
}
