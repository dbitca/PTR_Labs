package Week3.Actors

import akka.actor.Actor

class Actor_1 extends Actor{
  override def receive: Receive = {
    case message => println(message)
  }
}
