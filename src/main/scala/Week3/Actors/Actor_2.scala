package Week3.Actors

import akka.actor.Actor

class Actor_2 extends Actor {
  override def receive: Receive = {
    case message: String => println(message.toLowerCase().mkString)
    case message: Int => println((message.toInt + 1))
    case _ => println("I don't know how to handle this")
  }
}
