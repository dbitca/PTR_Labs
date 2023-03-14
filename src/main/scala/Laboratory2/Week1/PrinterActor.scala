package Week1

import akka.actor.{Actor, ActorLogging, Props}
import io.circe.Json
import io.circe.parser._
class PrinterActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case tweetData: String =>
     // println(s"Received tweet data: $tweetData")
      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          val tweetText = matched.group(1)
          println(s"Tweet text: $tweetText")
        case None =>
          println("No tweet text found.")
      }
      //generating random sleep time between 5ms and 50 ms
      val sleepTime = scala.util.Random.nextInt(46) + 5
      Thread.sleep(sleepTime)
  }
}

  object PrinterActor {
    def props(): Props = Props(new PrinterActor())
    case object Send
  }

