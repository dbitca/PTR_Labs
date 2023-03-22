package Laboratory2.Week2

import Week1.PrinterActor
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.protobufv3.internal.TextFormat.printer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import java.time.InstantSource.system
import scala.concurrent.duration.DurationInt

class Pool_Supervisor extends Actor{
  import Pool_Supervisor._
  var currentNrofTweets = 0
  var nrofActors = 3

  val strategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute) {
    case _: Exception => Restart
  }
  var printers: Seq[ActorRef] = (1 to nrofActors).map { i =>
    val printer = context.actorOf(Props[PrinterActor], s"printer$i")
    context.watch(printer)
    printer
  }

  var nextPrinterIndex = 0;

  context.system.scheduler.scheduleWithFixedDelay(
    0.seconds,
    10.seconds,
    self,
    AdjustActors
  )(context.dispatcher)
  override def receive: Receive = {
    //create printer pool
    case (message : String) =>
    {
      currentNrofTweets +=1
      val printer = printers (nextPrinterIndex)
      printer ! message
      //    context.system.scheduler.scheduleOnce(2.seconds, self, (message: String))
    }

    case AdjustActors =>{
      if(currentNrofTweets > nrofActors * 4){
        nrofActors += 1
        //        println("am intrat in case ul creste nr of Actors:" + nrofActors)
        val newPrinter = context.actorOf(Props[PrinterActor], s"printer$nrofActors")
        //        println("am creat un nou actor: " + s"printer$nrofActors")
        context.watch(newPrinter)
        printers = printers :+ newPrinter
        println(s"Added new printer, total nr of printers: $nrofActors")
      }
      else if(currentNrofTweets < nrofActors * 2){
        if(nrofActors > 1){
          val printerToKill = printers.last
          //          printerToKill ! akka.actor.PoisonPill
          printers = printers.filterNot(_ == printerToKill)
          nrofActors -=1
          //          println("am intrat in case-ul cu micsorarea nrlui de actori: " + nrofActors)
          println(s"Removed printer, total number of printers: $nrofActors")
        }
      }
      currentNrofTweets = 0
    }

      nextPrinterIndex = (nextPrinterIndex + 1) % nrofActors
  }
}
object Pool_Supervisor {
  def props(): Props = Props(new Pool_Supervisor())
  case object Print
  case object AdjustActors
}
