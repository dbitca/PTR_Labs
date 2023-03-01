package Week4.Actors

import akka.actor.{Actor, ActorRef}

class Editor_Actor (var next:ActorRef) extends Actor{
  override def receive: Receive ={
    case SetNextActor(actor) =>
      next = actor
    case message: List[String] => {
      println(message)
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
}
case class SetNextActor(actor:ActorRef)

