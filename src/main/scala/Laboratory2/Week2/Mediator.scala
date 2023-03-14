package Laboratory2.Week2

import akka.actor.{Actor, ActorRef, Props}

class Mediator (poolSupervisor: ActorRef) extends Actor{
  override def receive: Receive = {
    case message : String =>
      poolSupervisor ! message
  }
}

object Mediator{
  def props(poolSupervisor: ActorRef): Props = Props(new Mediator(poolSupervisor))
}
