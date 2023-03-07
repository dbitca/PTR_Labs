package Week4.Actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.util.ByteIterator.ByteArrayIterator.empty.next

class Split_Actor(next: ActorRef) extends Actor with ActorLogging {
  var words_list: List[String] = List.empty[String]

  override def receive: Receive = {
//    case SetNextActor(actor) =>
//      next = actor
    case message: String => {
      var new_message = message.trim.replaceAll(" +", " ")
      words_list = new_message.split(" ").toList
      println(words_list)
      next ! words_list
    }
    case message: '!' =>
      throw new Exception("I have been killed")
      Thread.sleep(500)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Restarting splitter")
  }
}
//case class SetNextActor(actor:ActorRef)



