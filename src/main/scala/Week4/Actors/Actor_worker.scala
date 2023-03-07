package Week4.Actors

import akka.actor.Actor

class Actor_worker extends Actor{
  override def receive: Receive = {
    case message => {
      if(message == "kill") throw new Exception("The worker actor is dead")
      println(s"${self.path.name} received message: $message")
    }
  }
}
