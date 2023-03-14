package Week3.Actors

import akka.actor.Actor

import scala.collection.immutable.Stream.Empty
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class Actor_5 extends Actor{
  var stack : ListBuffer[Int] = ListBuffer()
  override def receive: Receive = {
  case("push", n: Int) =>
      stack += n
      println("Ok")
  case ("pop", _) =>
      if(!stack.isEmpty){
        println(stack.last)
      }
      else println("The stack is empty")
  }
}
