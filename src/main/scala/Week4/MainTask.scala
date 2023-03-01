import Week4.Actors.{Editor_Actor, Join_Actor, SetNextActor, Split_Actor, Supervisor_Actor}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object MainTask extends App{
  val system = ActorSystem("my-actors2")
  val supervisor_Actor = system.actorOf(Props[Supervisor_Actor], "supervisor_Actor")

  supervisor_Actor ! "I am alive"
  supervisor_Actor ! '!'
  Thread.sleep(3000)
//  supervisor_Actor ! "I am alive"
//  system.terminate()
}