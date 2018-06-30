package interfaces

import infrastructure.Settings.settings

object Application extends App {
  val feedUrlList = Feeder.fetchUrlList(settings.feedUrl, settings.threshold)
  feedUrlList.foreach(println)
}
