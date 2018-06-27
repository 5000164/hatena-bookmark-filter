package interfaces

import com.softwaremill.sttp._

object Application extends App {
  val request = sttp.get(uri"http://b.hatena.ne.jp/hotentry/all.rss")
  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
  val response = request.send()
  println(response)
}
