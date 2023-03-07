package Week4.Actors

import akka.actor.{Actor, ActorLogging, ActorRef}

class Editor_Actor (next:ActorRef) extends Actor with ActorLogging{
  override def receive: Receive ={
//    case SetNextActor(actor) =>
//      next = actor
    case message: List[String] => {
//      println(message)
    var lowerCase = message.map(word => word.toLowerCase())
    var modifiedList = lowerCase.map(word =>
    if(word.contains('m') && word.contains('n')){
      word.map{
        case 'm' => 'n'
        case 'n' => 'm'
        case c => c
      }.mkString("")
    }  else {
      word
    }
    )
      next ! modifiedList
      println(modifiedList)
        }
    }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Restarting splitter")
  }
}
case class SetNextActor(actor:ActorRef)

