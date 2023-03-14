package Week4.Actors

import akka.actor.{Actor, ActorLogging, ActorRef}

class Join_Actor(next:ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case message: List[String] => {
      val finalMessage = message.mkString(" ")
      println(finalMessage)
    }
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Restarting joiner")
  }
}
