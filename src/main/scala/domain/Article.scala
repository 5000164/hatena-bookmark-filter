package domain

case class Article(
    url: String,
    date: String,
    hatenaBookmarkCount: Int,
    commentUrl: String,
    postChannelId: String,
    userName: String,
    iconEmoji: String)

case class DeliveredArticle(
    url: String,
    date: String)
