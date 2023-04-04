package Week2

import Week1.{PrinterActor, StreamReader}
import Week2.Pool_Supervisor.Print
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.protobufv3.internal.TextFormat.printer
import akka.stream.impl.fusing.Batch

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import java.time.InstantSource.system
import scala.concurrent.duration.DurationInt

class Pool_Supervisor extends Actor{
  import Pool_Supervisor._
  val strategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute) {
    case _: Exception => Restart
  }
  var currentNrofTweets = 0;
  var nextPrinterIndex = 0;
  var nrofActors = 3;

  val batcher = context.actorOf(Batcher.props(), "batcher")
  val aggregator = context.actorOf(Aggregator.props(batcher), "aggregator");
  val sentimentScoreActor = context.actorOf(Sentiment_Score_Actor.props(aggregator), "sentimentScoreActor")
  val engagementRatioActor = context.actorOf(Engagement_Ratio_Actor.props(aggregator), "engagementRatioActor")
  var printers: Seq[ActorRef] = (1 to nrofActors).map { i =>
    val printer = context.actorOf(PrinterActor.props(aggregator), s"printer$i")
    printer
  }

  context.system.scheduler.scheduleWithFixedDelay(
    3.seconds,
    10.seconds,
    self,
    AdjustActors
  )(context.dispatcher)
  override def receive: Receive = {
    //create printer pool
    case (message: String) =>
    {
      currentNrofTweets +=1
      val printer = printers (nextPrinterIndex)
      printer ! message
      sentimentScoreActor ! message
      engagementRatioActor ! message
      //    context.system.scheduler.scheduleOnce(2.seconds, self, (message: String))
    }

    case AdjustActors =>{
      //      println("current nr of tweets: " + currentNrofTweets)
      if(currentNrofTweets > 260){
        nrofActors += 1
        //        println("am intrat in case ul creste nr of Actors:" + nrofActors)
        val newPrinter = context.actorOf(PrinterActor.props(engagementRatioActor), s"printer$nrofActors")
        //context.watch(newPrinter)
        printers = printers :+ newPrinter
        println(s"Added new printer, total nr of printers: $nrofActors")
      }
      else if(currentNrofTweets < 220 ){
        if(nrofActors > 1){
          val printerToKill = printers.last
          //          println(">>>>>>>>>>>>>>>>>>>>"+ printerToKill)
          printerToKill ! akka.actor.PoisonPill
          //          println(printers)
          printers = printers.filterNot(_ == printerToKill)
          //          println(printers)
          nrofActors -=1
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
