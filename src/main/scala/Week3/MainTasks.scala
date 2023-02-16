package Week3
import Week3.Actors.Actor_5
import akka.actor._

object MainTasks extends App {
  val system = ActorSystem("my-actors2")
  val actor5 = system.actorOf(Props[Actor_5], "actor5")

  {
    Task5("push", 10)
    Task5("push", 5)
    Task5("pop", None)
  }
  def Task5(message: String, value: Any): Unit = {
    actor5 ! (message, value)
  }

  system.terminate()
}
