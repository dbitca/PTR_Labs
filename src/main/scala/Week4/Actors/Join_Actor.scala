package Week4.Actors

import akka.actor.{Actor, ActorRef}

class Join_Actor(next:ActorRef) extends Actor{
  override def receive: Receive = {
    case message: List[String] => {
      val finalMessage = message.mkString(" ")
      println(finalMessage)
    }
  }
}
