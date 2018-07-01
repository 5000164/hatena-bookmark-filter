package domain

case class Page(
    url: String,
    date: String,
    hatenaBookmarkCount: Int)

case class DeliveredPage(
    url: String,
    date: String)
