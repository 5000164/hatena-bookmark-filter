package domain

case class Page(
    url: String,
    date: String,
    hatenaBookmarkCount: Int,
    commentUrl: String,
    postChannelId: String,
    userName: String,
    iconEmoji: String)

case class DeliveredPage(
    url: String,
    date: String)
