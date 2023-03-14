package Week1

import Week1.StreamReader.Send
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ThrottleMode
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.stream.scaladsl.Sink

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class StreamReader(urlAddress: String, printerActor: ActorRef) extends Actor with ActorLogging {
  implicit val system = context.system
  implicit val dispatcher = context.dispatcher
  implicit val requestTimeout = akka.util.Timeout(5.second)

  override def receive: Receive = {
    case Send =>
      val request: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)

      val eventSource = EventSource(
        uri = urlAddress,
        send = request,
        initialLastEventId = None,
        retryDelay = 1.second
      )

      while (true) {
        val responseFuture = eventSource.throttle(1, 1.milliseconds, 1, ThrottleMode.Shaping)
          .take(10).runWith(Sink.seq)

        responseFuture.foreach(serverSentEvent => serverSentEvent.foreach(
          event => {
            val tweetData = event.getData()
            printerActor ! tweetData
          }
        ))
        while (!responseFuture.isCompleted) {}
      }
  }
}
object StreamReader {
  def props(urlAddress: String, printerActor: ActorRef): Props = Props(new StreamReader(urlAddress, printerActor))
  case object Send
}