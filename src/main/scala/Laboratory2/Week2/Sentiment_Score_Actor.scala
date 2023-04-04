package Week2

import akka.actor.{Actor, ActorRef, Props}

import java.net.{HttpURLConnection, URL}

class Sentiment_Score_Actor (next: ActorRef) extends Actor {

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
    case (tweetData: String) =>
      var tweet_id = ""
      var mean = 0.0
      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          var tweetText = matched.group(1)
          //     println(s"sentiment score received: $tweetText  and engagemenet ratio  $engagementRatio")
          val words = tweetText.toLowerCase().split("\\s+")
          val values = words.map(word => wordMap.getOrElse(word, 0))
          //          var mean = 0.00
          if (!values.isEmpty) {
            mean = values.sum / values.length.toDouble
          }
      }
      val id_pattern = """id_str"\s*:\s*"(\d+)""".r
      id_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          tweet_id = matched.group(1)
      }
      next ! ("SentimentScore", tweet_id, mean)
    //      println("Sentiment SCORE SENT")
  }
}

object Sentiment_Score_Actor {
  def props(next: ActorRef): Props = Props(new Sentiment_Score_Actor(next))
}

