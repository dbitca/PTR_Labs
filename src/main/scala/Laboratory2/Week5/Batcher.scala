package Laboratory2.Week5

import akka.actor.{Actor, Props}

class Batcher extends Actor{
  val batchSize = 10
  var tweets = new Array[String](batchSize)
  var count = 0
  override def receive: Receive = {
    case(tweet:String) =>
      tweets(count) = tweet
      count += 1
      if (count == batchSize) {
        println("Batch received:")
        tweets.foreach(println)
        tweets = new Array[String](batchSize)
        count = 0
      }
  }
}
object Batcher {
  def props(): Props = Props(new Batcher())
}
