package Laboratory2

import akka.actor.{Actor, ActorLogging, Props}
import io.circe.Json
import io.circe.parser._

import java.net.{HttpURLConnection, URL}
import java.util
class PrinterActor extends Actor with ActorLogging {
  private val bad_words: Array[String] = Array(
    "arse", "arsehead", "arsehole", "ass", "asshole", "bastard", "bitch", "bloody", "bollocks", "brotherfucker",
    "bugger", "bullshit", "child-fucker", "Christ on a bike", "Christ on a cracker", "cock", "cocksucker", "crap",
    "cunt", "damn", "damn it", "dick", "dickhead", "dyke", "fatherfucker", "frigger", "fuck", "goddamn", "godsdamn",
    "hell", "holy shit", "horseshit", "in shit", "Jesus Christ", "Jesus fuck", "Jesus H. Christ", "Jesus Harold Christ",
    "Jesus wept", "Jesus, Mary and Joseph", "kike", "motherfucker", "nigga", "nigra", "piss", "prick", "pussy", "shit",
    "shit ass", "shite", "sisterfucker", "slut", "son of a bitch", "son of a whore", "spastic", "sweet Jesus", "turd",
    "twat", "wanker", "fucking", "shitting")

  var text = ""
  val url = new URL("http://localhost:4000/emotion_values")
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  connection.setRequestMethod("GET")
  text = scala.io.Source.fromInputStream(connection.getInputStream).mkString
  var wordMap = Map[String, Int]()
  for (line <- text.split("\r\n")){
    var pair = line.split("\t") match {
      case Array(word, value) => (word, value.toInt)
    }
    wordMap += pair
  }
  override def receive: Receive = {
    case tweetData: String =>
      var followers_count = 0;
      var retweets_count = 0;
      var favourite_count = 0;

      val followers_count_pattern = """"followers_count":(\d+)""".r
      followers_count_pattern.findFirstMatchIn(tweetData) match{
        case Some(matched) =>
          followers_count = matched.group(1).toInt
        //          println(s"followers count: $followers_count")
      }
      val retweets_count_pattern = """"retweet_count":(\d+)""".r
      retweets_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          retweets_count = matched.group(1).toInt
        //          println(s"retweets count: $retweets_count")
      }

      val favourites_count_pattern = """"favourites_count":(\d+)""".r
      favourites_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          favourite_count = matched.group(1).toInt
        //          println(s"favourites count: $favourite_count")
      }

      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          var tweetText = matched.group(1)
          val words = tweetText.toLowerCase().split("\\s+")
          val values = words.map(word => wordMap.getOrElse(word, 0))
          var mean = 0.00
          if (!values.isEmpty)
            mean = values.sum / values.length.toDouble
          for (bad_word <- bad_words) {
            val bad_word_chars = bad_word.split("")
            val replacement = bad_word_chars.map(_ => "*").mkString("")
            tweetText = tweetText.split("\\s+")
              .map(word => if (word.toLowerCase == bad_word.toLowerCase) replacement else word)
              .mkString(" ")
          }
          var engagement_ratio = (favourite_count + retweets_count) / followers_count
          println(s"Tweet from:  " + self.path.name + s" $tweetText")
          println(s"Emotional value of tweet from: " + self.path.name + " is: " + mean)
          println(s"Engagement ratio of tweet from: " + self.path.name + " is: " + engagement_ratio)
          Thread.sleep(1000)
      }
    case None =>
      throw new Exception("No tweet text found message")
  }
  //generating random sleep time between 5ms and 50 ms
  val sleepTime = scala.util.Random.nextInt(46) + 5
  Thread.sleep(sleepTime)
}

object PrinterActor {
  def props(): Props = Props(new PrinterActor())
  case object Send
}

