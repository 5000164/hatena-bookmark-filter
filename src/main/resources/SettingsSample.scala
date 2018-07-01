import infrastructure.SettingsType

new SettingsType {
  override val feedUrl: String = "http://b.hatena.ne.jp/hotentry/all.rss"
  override val threshold: Int = 500
}
