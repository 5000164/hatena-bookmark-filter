package domain

case class Page(
    url: String,
    date: String,
    hatenaBookmarkCount: Int,
    commentUrl: String)

case class DeliveredPage(
    url: String,
    date: String)
