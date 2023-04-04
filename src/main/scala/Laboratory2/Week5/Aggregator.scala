package Laboratory2.Week5
import akka.actor.{Actor, ActorRef, Props}

class Aggregator(next: ActorRef) extends Actor {
  var tweets = Map.empty[String, Tweet]

  override def receive: Receive = {
    case ("EngagementRatio", id: String, engagementRatio: Double) => {
      //      println(s"Engagement Ratio score received by aggregator: $engagementRatio" + "with ID: " + id)
      val tweet = tweets.getOrElse(id, Tweet(id))
      tweets += id -> tweet.copy(engagementRatio = Some(engagementRatio))
      checkTweet(tweet.copy(engagementRatio = Some(engagementRatio)))
    }
    case ("SentimentScore", id: String, sentimentScore: Double) => {
      //      println(s"Sentiment Score received by aggregator: $sentimentScore" + "with ID: " + id)
      val tweet = tweets.getOrElse(id, Tweet(id))
      tweets += id -> tweet.copy(sentimentScore = Some(sentimentScore))
      checkTweet(tweet.copy(sentimentScore = Some(sentimentScore)))
    }

    case (id: String, tweetText: String) => {
      //      println("Tweet received by aggregator: " + tweetText + "with ID: " + id)
      val tweet = tweets.getOrElse(id, Tweet(id))
      tweets += id -> tweet.copy(tweetText = Some(tweetText))
      checkTweet(tweet.copy(tweetText = Some(tweetText)))
    }
  }

  def checkTweet(tweet: Tweet): Unit = {
    tweet match {
      case Tweet(id, Some(engagementRatio), Some(sentimentScore), Some(tweetText)) =>
        //        println(s"Tweet received by aggregator: id=$id, engagementRatio=$engagementRatio, sentimentScore=$sentimentScore, tweetText=$tweetText")
        val tweetString = s"id=$id, engagementRatio=$engagementRatio, sentimentScore=$sentimentScore, tweetText=$tweetText"
        next ! tweetString
        tweets -= id
      case _ =>
    }
  }
}

case class Tweet(id: String, engagementRatio: Option[Double] = None, sentimentScore: Option[Double] = None, tweetText: Option[String] = None)

object Aggregator {
  def props(next:ActorRef): Props = Props(new Aggregator(next))
}