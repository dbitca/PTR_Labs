package Week2

import akka.actor.{Actor, Props}

import java.net.{HttpURLConnection, URL}

class Sentiment_Score_Actor extends Actor{

  var text = ""
  val url = new URL("http://localhost:4000/emotion_values")
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  connection.setRequestMethod("GET")
  text = scala.io.Source.fromInputStream(connection.getInputStream).mkString
  var wordMap = Map[String, Int]()
  for (line <- text.split("\r\n")) {
    var pair = line.split("\t") match {
      case Array(word, value) => (word, value.toInt)
    }
    wordMap += pair
  }
  override def receive: Receive = {
    case (tweetText:String, engagementRatio: Double) =>
      //      println(s"sentiment score received: $tweetText  and engagemenet ratio  $engagementRatio")
      val words = tweetText.toLowerCase().split("\\s+")
      val values = words.map(word => wordMap.getOrElse(word, 0))
      var mean = 0.00
      if (!values.isEmpty) {
        mean = values.sum / values.length.toDouble
      }
      println(s"Tweet data: $tweetText")
      println(s"Engagement ratio: $engagementRatio")
      println(s"Sentimental score: $mean")
  }
}

object Sentiment_Score_Actor {
  def props(): Props = Props(new Sentiment_Score_Actor())
}

