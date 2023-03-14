package Laboratory2.Week2

import Week1.{PrinterActor, StreamReader}
import Week2.Pool_Supervisor.Print
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.protobufv3.internal.TextFormat.printer

import java.time.InstantSource.system
import scala.concurrent.duration.DurationInt

class Pool_Supervisor extends Actor{
  import Pool_Supervisor._

  val strategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute) {
    case _: Exception => Restart
  }
  val printers: Seq[ActorRef] = (1 to 3).map { i =>
    val printer = context.actorOf(Props[PrinterActor], s"printer$i")
    context.watch(printer)
    printer
  }

  var nextPrinterIndex = 0;
  override def receive: Receive = {
 //create printer pool
    case (message : String) =>
  {
    val printer = printers (nextPrinterIndex)
    printer ! message
  }
      nextPrinterIndex = (nextPrinterIndex + 1) % 3
  }
}
object Pool_Supervisor {
  def props(): Props = Props(new Pool_Supervisor())
  case object Print
}
