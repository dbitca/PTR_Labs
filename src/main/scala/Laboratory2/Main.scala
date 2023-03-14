import Week1.{PrinterActor, StreamReader}
import Week2.{Mediator, Pool_Supervisor}
import akka.actor

object Main {
  def main(args: Array[String]): Unit = {
    val system = actor.ActorSystem("StreamReaderSystem")

    val tweets1 = "http://localhost:4000/tweets/1"
    val tweets2 = "http://localhost:4000/tweets/2"

    // val printerActor = system.actorOf(TweetPrinter.props(30.milliseconds), "TweetPrinter")
    val printerActor = system.actorOf(PrinterActor.props(), "printerActor")
    val pool_Supervisor = system.actorOf(Pool_Supervisor.props(), "pool_Supervisor")
    val mediator = system.actorOf(Mediator.props(pool_Supervisor), "mediator")
    val streamReader = system.actorOf(StreamReader.props(tweets1, mediator), "StreamReader")
    val streamReader2 = system.actorOf(StreamReader.props(tweets2, mediator), "StreamReader2")
    streamReader ! StreamReader.Send
    streamReader2 ! StreamReader.Send
  }
}
