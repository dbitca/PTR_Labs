package Week1

import akka.actor.{Actor, ActorLogging, Props}
import io.circe.Json
import io.circe.parser._

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
  override def receive: Receive = {

    case tweetData: String =>
      //     println(s"Received tweet data: $tweetData")
      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          var tweetText = matched.group(1)
          for (bad_word <- bad_words) {
            val bad_word_chars = bad_word.split("")
            val replacement = bad_word_chars.map(_ => "*").mkString("")
            tweetText = tweetText.split("\\s+")
              .map(word => if (word.toLowerCase == bad_word.toLowerCase) replacement else word)
              .mkString(" ")
          }
          println(s"Tweet from:  " + self.path.name + s" $tweetText")
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

