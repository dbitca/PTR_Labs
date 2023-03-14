import Week3.Actors.{Actor_1, Actor_2, Actor_4, Actor_Monitor, Actor_Worker, RegisterActor}
import akka.actor.{ActorSystem, Props}

object MinimalTasks extends App {
  val system = ActorSystem("my-actors")
  val actor1 = system.actorOf(Props[Actor_1], "actor1")
  val actor2 = system.actorOf(Props[Actor_2], "actor2")
  val actor_monitor = system.actorOf(Props[Actor_Monitor], "monitor-actor")
  val actor_worker = system.actorOf(Props[Actor_Worker], "actor_worker")
  val actor4 = system.actorOf(Props[Actor_4], "actor4")

  {
    Task1("Hello World")
    Task2("HELLO WORLD")
    Task2(10)
    Task2(List(10, 19, 0))
    Task3("start")
    Task3("stop")
    Task4(10)
    Task4(10)
    Task4(10)
  }
  def Task1(message: String): Unit = {
    actor1 ! message
  }
  def Task2(message: Any): Unit = {
    actor2 ! message
  }
  def Task3(message: String): Unit = {
    actor_monitor ! RegisterActor(actor_worker)
    actor_worker ! message
  }
  def Task4(message: Int): Unit = {
    actor4 ! message
  }

  system.terminate()
}