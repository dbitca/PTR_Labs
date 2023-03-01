package Week4.Actors

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, AllForOneStrategy, OneForOneStrategy, Props, Terminated}
import akka.routing.ActorRefRoutee

class Supervisor_Actor extends Actor with ActorLogging{
  private val split_Actor = context.actorOf(Props(new Split_Actor(null)), "split_actor")
  context.watch(split_Actor)
  private val editor_Actor = context.actorOf(Props(new Editor_Actor(null)), "editor_actor")
  context.watch(editor_Actor)
  private val join_Actor = context.actorOf(Props(new Join_Actor(null)), "join_actor")
  context.watch(join_Actor)

  split_Actor ! SetNextActor(editor_Actor)
  editor_Actor ! SetNextActor(join_Actor)
  override def receive: Receive = {
    case message =>
      split_Actor.forward(message)

    case Terminated(ref) =>
      println(ref.path.name)

  }

  override val supervisorStrategy = OneForOneStrategy(){
    case _: Exception => Restart
  }
}
