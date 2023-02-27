package Week4

import akka.actor.Actor

class Actor_worker extends Actor{
  override def receive: Receive = {
    case message => {
      if(message == "kill") throw new Exception("dodicu is dead")
      println(s"${self.path.name} received message: $message")
    }
  }
}
