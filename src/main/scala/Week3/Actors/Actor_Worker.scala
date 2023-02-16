package Week3.Actors

import akka.actor.Actor

class Actor_Worker extends Actor{
  override def receive: Receive = {
    case "start" => println("Worker actor is working...")
    case "stop" => context.stop(self)
  }
}
