package interfaces

object Application extends App {
  val feedUrlList = Feeder.fetchUrlList()
  feedUrlList.foreach(println)
}
