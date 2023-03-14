package Week4.Actors

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, OneForOneStrategy, Props, Terminated}

import scala.concurrent.duration._

class Supervisor extends Actor{
  val strategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute){
    case _: Exception => Restart
  }
  override def receive: Receive ={
    case ("CreateWorkers", n: Int) =>
      for (i <- 1 to n){
        val worker = context.actorOf(Props[Actor_worker], s"worker$i")
        context.watch(worker)
      }
      case(message:String, i: Int) => {
        context.child(s"worker$i") match {
          case Some(worker) => worker ! message
          case None => println(s"Worker $i does not exist!")
        }
      }
        case Terminated(actor) =>
        {
          println(s"Actor ${actor.path.name} has died!")
        }
    case _ => {
      println("I don't know what to do with this")
    }
  }
}
