package Week3
import Week3.Actors.{Actor_5, Mutex_Actor}
import akka.actor._

object MainTasks extends App {
  val system = ActorSystem("my-actors2")
  val actor5 = system.actorOf(Props[Actor_5], "actor5")
  val mutex_Actor = system.actorOf(Props[Mutex_Actor], "mutex_Actor")

  {
    Task5("push", 10)
    Task5("push", 5)
    Task5("pop", None)
    Semaphore("create_semaphore", 3)
    Semaphore("acquire", None)
    Semaphore("acquire", None)
    Semaphore("acquire", None)
    Semaphore("release", None)
    Semaphore("release", None)
  }
  def Task5(message: String, value: Any): Unit = {
    actor5 ! (message, value)
  }

  def Semaphore(message: String, value: Any): Unit = {
  mutex_Actor ! (message, value)
  }
  system.terminate()
}
