import Week4.Actors.{Actor_worker, Supervisor}
import akka.actor.{ActorRef, ActorSystem, Props}
object MinTasks extends App {
  val system = ActorSystem("my-actors")
  val supervisor = system.actorOf(Props[Supervisor], "supervisor")

  supervisor ! ("CreateWorkers", 2)
  supervisor ! ("I am worker and i received", 1)
  supervisor ! ("I am worker and i received", 2)
  supervisor ! ("I am worker and i received", 3)
  supervisor ! ("I am worker and i received", 1)
  supervisor ! ("kill", 2)
  Thread.sleep(1000)
  supervisor ! ("I am alive", 2)

  system.terminate()
}
