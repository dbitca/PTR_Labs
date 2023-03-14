package Week3.Actors

import akka.actor.{Actor, ActorRef, Terminated}
class Actor_Monitor extends Actor {
  override def receive: Receive = {
    case RegisterActor(actor) =>
      context.watch(actor)
    case Terminated(actor) =>
      println("Actor " + actor.path.name +
      " stopped working")
  }
}
case class RegisterActor(actor: ActorRef)