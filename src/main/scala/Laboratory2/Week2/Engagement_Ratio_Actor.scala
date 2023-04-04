package Week2

import akka.actor.{Actor, ActorRef, Props}

class Engagement_Ratio_Actor (next: ActorRef) extends Actor{
  override def receive: Receive = {
    case (tweetData: String) => {
      //      println(s"Engagement received : $tweetText")
      var followers_count = 0;
      var retweets_count = 0;
      var favourite_count = 0;
      var engagement_ratio = 0.00;
      var tweet_id = ""

      val followers_count_pattern = """"followers_count":(\d+)""".r
      followers_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          followers_count = matched.group(1).toInt
        //          println(s"followers count: $followers_count")
      }
      val retweets_count_pattern = """"retweet_count":(\d+)""".r
      retweets_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          retweets_count = matched.group(1).toInt
        // println(s"retweets count: $retweets_count")
      }

      val favourites_count_pattern = """"favourites_count":(\d+)""".r
      favourites_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          favourite_count = matched.group(1).toInt
        // println(s"favourites count: $favourite_count")
      }
      if(followers_count != 0) {
        engagement_ratio = (favourite_count + retweets_count) / followers_count
      }
      val id_pattern = """id_str"\s*:\s*"(\d+)""".r
      id_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          tweet_id = matched.group(1)
      }
      next ! ("EngagementRatio", tweet_id, engagement_ratio)
    }
  }
}
//
object Engagement_Ratio_Actor {
  def props(next: ActorRef): Props = Props(new Engagement_Ratio_Actor(next))
}
