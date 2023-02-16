package Week3.Actors

import akka.actor.Actor

class Actor_4 extends Actor {
  var average: Float = 0.0f

  override def receive: Receive = {
    case message: Int => var new_average = (average + message) / 2.0f
      average = new_average
      println("New average: " + average)
  }
}
