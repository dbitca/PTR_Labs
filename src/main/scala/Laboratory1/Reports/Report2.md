# FAF.PTR16.1 -- Project 1
> **Performed by:** Bitca Dina, group FAF-201
> **Verified by:** asist. univ. Alexandru Osadcenco

## P1W1

The first week's laboratory work consisted of three minimal tasks, two main tasks and two bonus tasks. For the sake 
of consistency and conciseness, I will only be explaining the main functions used in order to complete these tasks.

**Task 1**

The three minimal tasks consisted of creating a VCS repository, writing an actor that would read the SSE stream 
available on Docker Hub and creating a second actor that would print the received streams.
For the Reader Actor, the most important functions are exhibited in the code snippets below.
```scala
class StreamReader(urlAddress: String, printerActor: ActorRef) extends Actor with ActorLogging {
  implicit val system = context.system
  implicit val dispatcher = context.dispatcher
  implicit val requestTimeout = akka.util.Timeout(5.second)

  override def receive: Receive = {
    case Send =>
      val request: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)

      val eventSource = EventSource(
        uri = urlAddress, 
        initialLastEventId = None, 
        retryDelay = 1.second 
      )
    
      while (true) {
        val responseFuture = eventSource.throttle(1, 1.milliseconds, 1, ThrottleMode.Shaping)
          .take(10).runWith(Sink.seq)

        responseFuture.foreach(serverSentEvent => serverSentEvent.foreach(
          event => {
            val tweetData = event.getData()
            printerActor ! self.path.name + " " + tweetData
          }
        ))
        while (!responseFuture.isCompleted) {}
      }
  }
}

```
The "Stream Reader" actor connects to the URL Address of the docker image and listens for Server-Sent Events (SSEs). SSEs are pools of data sent from the server to a client over the long-lived HTTP connection. The actor uses HTTP to create an HTTP request and then uses the EventSource class to set up additional parameters such as the initial value for the Last-Event-Id header and the delay between retries to establish the connection. After the connection is establish, the actor continues to listen for incoming SSEs in an infinite loop. The resposeFuture is set up to throttle  incoming events before collectiong them into a sequence of 10. For each event received, the data is extracted and a message is sent to the "Printer" actor, along with the actor's name.
The "Printer" actor is exhibited in the code snippet below. 

```scala
class PrinterActor (next: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case tweetData: String =>
      var tweetText = ""
      var tweet_id = ""
      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          tweetText = matched.group(1)
          println(tweetText)
          Thread.sleep(1000)

        case None =>
          throw new Exception("No tweet text found message")
      }
  }
```
The "Printer" actor receives the tweet data in the form of a string message. When a message is received, the actor uses a regular expression pattern matching to extract the tweet text. It then prints the extracted tweet text to the console and waits for 1 second. The waiting time has been set up in order to check for the proper function of the code. If the pattern matching fails to find a match in the message, the actor throws an exception with the message "No tweet text found".

**Task 2** 

The main tasks consist of creating a second "Reader" actor that consumes the second stream provided by the Docker 
image and send the tweets to the same Printer actor. Afterwards, the "Printer" actor needs to simulate some load on 
the actor by sleeping every time a tweet is received.
The first main task is executed in the main section of the code. A new Stream Reader is initialized in the same way 
as the first Stream Reader, and a new URL is added. 
Please check the code snippet below:
```scala
def main(args: Array[String]): Unit = {
  val system = actor.ActorSystem("StreamReaderSystem")

  val tweets1 = "http://localhost:4000/tweets/1"
  val tweets2 = "http://localhost:4000/tweets/2"

  val streamReader = system.actorOf(StreamReader.props(tweets1), "StreamReader")
  val streamReader2 = system.actorOf(StreamReader.props(tweets2), "StreamReader2")
  streamReader ! StreamReader.Send
  streamReader2 ! StreamReader.Send
}}
```
The main function then creates two String variables, tweets1 and tweets2, each representing a URL that points to a server that provides SSEs.
Next, the main function creates two instances of the StreamReader actor using the StreamReader.props function, passing in the URL as argument. The two StreamReader actors are then given unique names "StreamReader" and "StreamReader2" using the actorOf method of the ActorSystem object.
Finally, the main function sends the Send message to both StreamReader actors using the ! operator.

The load simulation on the Printer actor can be observed in the code snippet below. 

```scala
 val sleepTime = scala.util.Random.nextInt(46) + 5
```
This line generates a random value of sleep time between 5 ms to 50 ms in order to simulate load on the printer actor.
## P1W2

The second week of this laboratory work consists of two minimal tasks, one main task and a bonus task. The minimal 
and main tasks will be described in the snippets below. 

**Task 1** 

The minimal tasks consist of creating a Worker Pool to substitute the Printer actor. The Worker Pool is 
supervised by a Pool Supervisor. The tweets are sent to the printers in a Round Robin fashion, so all the printers 
would continue working efficiently.
The code snippet for the first task is represented below:
```scala
class Pool_Supervisor extends Actor{
  import Pool_Supervisor._
  val strategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute) {
    case _: Exception => Restart
  }

  var nextPrinterIndex = 0;
  var nrofActors = 3;

  var printers: Seq[ActorRef] = (1 to nrofActors).map { i =>
    val printer = context.actorOf(PrinterActor.props(), s"printer$i")
    printer
  }

  override def receive: Receive = {

    case (message: String) =>
      {
        val printer = printers (nextPrinterIndex)
        printer ! message
      }
      nextPrinterIndex = (nextPrinterIndex + 1) % nrofActors
  }}
```
The actor Pool_Supervisor supervises a pool of PrinterActor instances. In the first lines, the OneForOneStrategy is used to define a maximum number of retries of 3 and a time range of 1 minute. In this case, the strategy restarts the failed actor up to three times within a one-minute time window. The nrofActors variable is used to define the total number of actors in the pool, which is set to three in this example.
In the receive method, the Pool_Supervisor actor listens for incoming messages of type String. When a message is received, the Pool_Supervisor actor selects the next PrinterActor in the round-robin sequence using the nextPrinterIndex counter. The selected PrinterActor is then sent the received message using the ! operator. The nextPrinterIndex counter is incremented by one and then wraps back to zero when it exceeds the nrofActors value, which ensures that messages are sent in a round-robin fashion to all of the actors in the pool.
Here goes the explanation of the code from above..

**Task 2** 

The main task consists of setting the worker actor to crash when a "kill message" is sent in the stream.
This happens automatically in the throttle process by increasing the volume of messages in the throttle. Once an 
empty message is received, the actor sends a crashing message. No changes had to be made to my original 
implementation to achieve this.

## P1W3

The third week's laboratory work consists of one minimal task, one main task and a bonus task. 

**Task 1**

For the first task, we had to continue the Worker actor. Any bad words that a tweet might contain
mustn’t be printed. Instead, a set of stars should appear, the number of which corresponds to
the bad word’s length.
Please see the code snippet below for the implementation of the first task.

```scala
  private val bad_words: Array[String] = Array(
    ...)
  override def receive: Receive = {
    case tweetData: String =>
      var tweetText = ""
      var tweet_id = ""
      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          tweetText = matched.group(1)
          for (bad_word <- bad_words) {
            val bad_word_chars = bad_word.split("")
            val replacement = bad_word_chars.map(_ => "*").mkString("")
            tweetText = tweetText.split("\\s+")
              .map(word => if (word.toLowerCase == bad_word.toLowerCase) replacement else word)
              .mkString(" ")
          }}}
      next ! (tweetText)
      Thread.sleep(1000)
```
Please note that I have not added the words in the bad words array to this code snippet for the sake of conciseness.
The code snippet contains an array of bad words that are to be filtered out, and a regular expression pattern that matches the text of a tweet. When a tweet is received, the regular expression is used to extract the text of the tweet, and then each word in the text is checked against the array of bad words. If a word matches a bad word, it is replaced with a string of asterisks of the same length as the original word.
The filtered tweet text is then sent to the next actor using the ! operator, and the thread is put to sleep for one second before processing the next tweet.

**Task 2**

The main task consists of creating an actor that would manage the number of Worker actors in the Worker
Pool. When the actor detects an increase in incoming tasks, it should instruct the Worker Pool
to also increase the number of workers. Conversely, if the number of tasks in the stream is low,
the actor should dictate to reduce the number of workers.
The implementation for this task can be observed in the code snippet below:
```scala
    case AdjustActors =>{
      if(currentNrofTweets > 260){
        nrofActors += 1
        val newPrinter = context.actorOf(PrinterActor.props(engagementRatioActor), s"printer$nrofActors")
        printers = printers :+ newPrinter
        println(s"Added new printer, total nr of printers: $nrofActors")
      }
      else if(currentNrofTweets < 220 ){
        if(nrofActors > 1){
        val printerToKill = printers.last
          printerToKill ! akka.actor.PoisonPill
         printers = printers.filterNot(_ == printerToKill)
          nrofActors -=1
          println(s"Removed printer, total number of printers: $nrofActors")
        }
      }
```
Please note that this code snippet has been implemented in the "Worker Pool" actor and the "currentnroftweets" 
variable is incremented each time a new message is received.
When the "AdjustActors" message is received, the actor checks the current number of tweets and adjusts the number of 
actors (i.e. 
printers) accordingly.

If the current number of tweets is greater than 260, the actor creates a new printer actor and adds it to the list of printers. The number of actors is incremented and a message is printed to the console indicating that a new printer has been added.

If the current number of tweets is less than 220 and there is more than one actor, the actor removes the last printer actor in the list of printers using the PoisonPill message. The number of actors is decremented and a message is printed to the console indicating that a printer has been removed.

## P1W4

The fourth week of the laboratory work consists of one minimal task, three main tasks and one bonus task. The 
implementation for the minimal and main tasks will be described in the snippets below.

**Task 1**

The minimal task consists of continuing the Working actor. Besides printing out the redacted tweet text,
the Worker actor must also calculate two values: the Sentiment Score and the Engagement
Ratio of the tweet. To compute the Sentiment Score per tweet you should calculate the mean
of emotional scores of each word in the tweet text.
The updated printer actor can be observed in the code snippet below:
```scala
class PrinterActor extends Actor with ActorLogging {
  private val bad_words: Array[String] = Array(
  ...)

  var text = ""
  val url = new URL("http://localhost:4000/emotion_values")
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  connection.setRequestMethod("GET")
  text = scala.io.Source.fromInputStream(connection.getInputStream).mkString
  var wordMap = Map[String, Int]()
  for (line <- text.split("\r\n")){
    var pair = line.split("\t") match {
      case Array(word, value) => (word, value.toInt)
    }
    wordMap += pair
  }
  override def receive: Receive = {
    case tweetData: String =>
      var followers_count = 0;
      var retweets_count = 0;
      var favourite_count = 0;

      val followers_count_pattern = """"followers_count":(\d+)""".r
      followers_count_pattern.findFirstMatchIn(tweetData) match{
        case Some(matched) =>
          followers_count = matched.group(1).toInt
      }
      val retweets_count_pattern = """"retweet_count":(\d+)""".r
      retweets_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          retweets_count = matched.group(1).toInt
      }

      val favourites_count_pattern = """"favourites_count":(\d+)""".r
      favourites_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          favourite_count = matched.group(1).toInt
      }

      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          var tweetText = matched.group(1)
          val words = tweetText.toLowerCase().split("\\s+")
          val values = words.map(word => wordMap.getOrElse(word, 0))
          var mean = 0.00
          if (!values.isEmpty)
            mean = values.sum / values.length.toDouble
          for (bad_word <- bad_words) {
            val bad_word_chars = bad_word.split("")
            val replacement = bad_word_chars.map(_ => "*").mkString("")
            tweetText = tweetText.split("\\s+")
              .map(word => if (word.toLowerCase == bad_word.toLowerCase) replacement else word)
              .mkString(" ")
          }
          var engagement_ratio = (favourite_count + retweets_count) / followers_count
          println(s"Tweet from:  " + self.path.name + s" $tweetText")
          println(s"Emotional value of tweet from: " + self.path.name + " is: " + mean)
          println(s"Engagement ratio of tweet from: " + self.path.name + " is: " + engagement_ratio)
          Thread.sleep(1000)
      }
    case None =>
      throw new Exception("No tweet text found message")
  }
```
When a tweet is received by the actor, it extracts various pieces of information from the tweet's JSON data, such as the number of followers, retweets, favorites, and the actual tweet text. The actor then processes the tweet text by mapping each word to its emotional value, which is retrieved from a local server via a GET request. The emotional values of each word are averaged to obtain an emotional value for the tweet as a whole. The tweet text is also checked for any "bad words" defined in the bad_words array, and any instances of these bad words are replaced with asterisks. Finally, the actor calculates the engagement ratio of the tweet and prints out the tweet text, its emotional value, and its engagement ratio.

The "PrinterActor" also defines some instance variables such as text, which is used to store the JSON response from the server and wordMap which is used to store the word-emotion mappings. The receive method of the actor listens for incoming tweet data and processes it accordingly. If no tweet text is found, an exception is thrown.

**Task 2**

The main tasks consist of breaking up the logic of the current worker in three separate actors.
The code snippets for the implementation of this task is represented below.

Code snippet for Engagement_Ratio Actor:
```scala
class Engagement_Ratio_Actor (next: ActorRef) extends Actor{
  override def receive: Receive = {
    case (tweetData: String) => {
      var followers_count = 0;
      var retweets_count = 0;
      var favourite_count = 0;
      var engagement_ratio = 0.00;
      var tweet_id = ""

      val followers_count_pattern = """"followers_count":(\d+)""".r
      followers_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          followers_count = matched.group(1).toInt
      }
      val retweets_count_pattern = """"retweet_count":(\d+)""".r
      retweets_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          retweets_count = matched.group(1).toInt
       
      }
      val favourites_count_pattern = """"favourites_count":(\d+)""".r
      favourites_count_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          favourite_count = matched.group(1).toInt
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
  }}
```
The engagement_score actor receives a tuple message containing a tweet data as a String and sends a message to the next 
actor with the format of ("EngagementRatio", tweet_id, engagement_ratio).

In the "receive" method, the tweet data is matched with regular expressions to extract the followers_count, retweets_count, favourite_count, and tweet_id values. The engagement_ratio is calculated as the sum of favourite_count and retweets_count divided by followers_count only if followers_count is not zero.

Finally, a message containing the engagement ratio and tweet ID is sent to the next actor using next ! ("EngagementRatio", tweet_id, engagement_ratio).

The code snippet for the "Sentiment Score" Actor can be seen in the snippet below:

```scala
class Sentiment_Score_Actor (next: ActorRef) extends Actor {

  var text = ""
  val url = new URL("http://localhost:4000/emotion_values")
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  connection.setRequestMethod("GET")
  text = scala.io.Source.fromInputStream(connection.getInputStream).mkString
  var wordMap = Map[String, Int]()
  for (line <- text.split("\r\n")) {
    var pair = line.split("\t") match {
      case Array(word, value) => (word, value.toInt)
    }
    wordMap += pair
  }

  override def receive: Receive = {
    case (tweetData: String) =>
      var tweet_id = ""
      var mean = 0.0
      val pattern = """"text":"(.+?)"""".r
      pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          var tweetText = matched.group(1)
          val words = tweetText.toLowerCase().split("\\s+")
          val values = words.map(word => wordMap.getOrElse(word, 0))
          if (!values.isEmpty) {
            mean = values.sum / values.length.toDouble
          }}
      val id_pattern = """id_str"\s*:\s*"(\d+)""".r
      id_pattern.findFirstMatchIn(tweetData) match {
        case Some(matched) =>
          tweet_id = matched.group(1)
      }
      next ! ("SentimentScore", tweet_id, mean)
  }}
```
The actor first initializes the text variable to hold the content of the HTTP GET request to the external API. The API returns a tab-separated list of words and their associated sentiment scores, which the actor then processes and stores in a wordMap dictionary. The wordMap dictionary is a mapping of words to sentiment scores.

The receive method of the actor takes in the tweet data as a string and extracts the text of the tweet using a regular expression pattern match. It then converts the text to lowercase and splits it into individual words. For each word, it looks up the associated sentiment score from the wordMap dictionary and creates an array of sentiment scores for each word in the tweet. The actor then calculates the mean sentiment score for the tweet by taking the sum of the sentiment scores and dividing by the number of words in the tweet.

The actor then extracts the tweet ID from the tweet data using another regular expression pattern match and sends a message to the next actor in the pipeline containing the sentiment score and the tweet ID.

## P1W5

The laboratory work for the fifth week consists of one minimal task, two main tasks and two bonus tasks.

**Task 1**

The minimal task consists of creating an actor that would  collect the redacted tweets from Workers and
would print them in batches. Instead of printing the tweets, the Worker should now send them
to the Batcher, which then prints them.

The code snippet for the minimal task can be seen in the code snippet below:
```scala
class Aggregator(next: ActorRef) extends Actor {
  var tweets = Map.empty[String, Tweet]

  override def receive: Receive = {
    case ("EngagementRatio", id: String, engagementRatio: Double) => {
val tweet = tweets.getOrElse(id, Tweet(id))
      tweets += id -> tweet.copy(engagementRatio = Some(engagementRatio))
      checkTweet(tweet.copy(engagementRatio = Some(engagementRatio)))
    }
    case ("SentimentScore", id: String, sentimentScore: Double) => {
val tweet = tweets.getOrElse(id, Tweet(id))
      tweets += id -> tweet.copy(sentimentScore = Some(sentimentScore))
      checkTweet(tweet.copy(sentimentScore = Some(sentimentScore)))
    }
    case (id: String, tweetText: String) => {
val tweet = tweets.getOrElse(id, Tweet(id))
      tweets += id -> tweet.copy(tweetText = Some(tweetText))
      checkTweet(tweet.copy(tweetText = Some(tweetText)))
    }
  }

  def checkTweet(tweet: Tweet): Unit = {
    tweet match {
      case Tweet(id, Some(engagementRatio), Some(sentimentScore), Some(tweetText)) =>
        val tweetString = s"id=$id, engagementRatio=$engagementRatio, sentimentScore=$sentimentScore, tweetText=$tweetText"
        next ! tweetString
        tweets -= id
      case _ =>
    }
  }
}
```
The "receive" method of the Aggregator class expects tuples that represent different types of tweet data, received 
from the three different actors that send the tweet message, the engagement ratio and the sentiment score.
If the received data is an engagement ratio or a sentiment score, the tweet ID and corresponding value are extracted and used to update the tweets variable. The updated tweet is then passed to the checkTweet method for processing. If the received data is tweet text, the tweet ID and text are extracted, the tweets variable is updated, and the updated tweet is passed to the checkTweet method for processing.

The "checkTweet" method of the Aggregator class expects a tweet of type Tweet and checks if all tweet data is available. If all tweet data is available, a string representation of the tweet data is created and sent to the next actor in the pipeline. The tweet is then removed from the tweets variable.

**Task 2**
The main task is to continue the batcher actor. If, in a given time window, the Batcher does not
receive enough data to print a batch, it should still print it.
The code snippet for the implementation of the main task is represented below:

```scala
class Batcher(batchSize: Int, timeWindow: FiniteDuration) extends Actor{

  var tweets = new Array[String](batchSize)
  var count = 0
  var timer: Option[Cancellable] = None


  override def preStart(): Unit = {
    timer = Some(context.system.scheduler.scheduleAtFixedRate(
      initialDelay = timeWindow,
      interval = timeWindow,
      receiver = self,
      message = "TimeOut"))
  }

  override def postStop(): Unit = {
    timer.foreach(_.cancel())
  }

  import ExecCtx._

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
    case "TimeOut" =>
      if(count > 0){
        println("Time out")
        tweets.take(count).foreach(println)
        tweets = new Array[String](batchSize)
        count = 0
      }
  }
}
```
The Batcher class's receive() method is implemented to receive incoming tweets. Each tweet is added to the tweets array, and the count variable is incremented. When the count variable reaches the batchSize value, the tweets are printed out as a batch, and the tweets array and count variable are reset.

The "receive()" method also handles the case where the timer expires before the batch size is reached. In this case, any tweets in the current batch are printed out as a batch, and the tweets array and count variable are reset.

## P1W6

The laboratory work for week six consists of two minimal tasks, one main task and one bonus task. Further, only the 
minimal task will be explained.

**Task 1**

The first task consists of creating a database and adding the tweets to that specific database.
I have created a database using Slick library and a docker image.
The code snippets for the model of the table can be seen below:

```scala
case class TweetEntry(tweet_id: Long, tweet: String)
object SlickTables {
  import slick.jdbc.PostgresProfile.api._

  class TweetTable(tag: Tag) extends Table[TweetEntry](tag, Some("tweets"), "TweetEntry") {
    def id = column[Long]("tweet_id", O.PrimaryKey, O.AutoInc)

    def tweet = column[String]("tweet")
    
    override def * = (id, tweet) <> (TweetEntry.tupled, TweetEntry.unapply)
  }
  lazy val tweetTable = TableQuery[TweetTable]
}
```

The TweetEntry class defines a Slick table to represent tweet entries. The TweetEntry case class represents the structure of each tweet entry, with a tweet ID and a string representing the tweet. The SlickTables object defines a TweetTable class that extends the Table class provided by Slick, specifying the schema of the tweet entries table. The id and tweet columns are defined using the column method, with the id column set as the primary key and auto-incremented. The * method defines the projection of the table to the TweetEntry case class, specifying how to map the table columns to the case class fields using the <> operator. The tweetTable value is created using TableQuery method with TweetTable as a type parameter, which allows to perform operations on the table such as querying and inserting data.

## Conclusion

In conclusion, the examples provided demonstrate the power of actors in Scala for building concurrent and distributed systems. By creating isolated and independent units of computation that communicate through message passing, actors enable a more resilient and fault-tolerant system. The code snippets showed how actors can be used for a variety of tasks, from simple message printing to implementing more complex tasks such as a mutex and a supervisor for managing worker actors. Furthermore, actors can be supervised and restarted in case of failures, adding an extra layer of reliability to the system. Overall, Scala actors offer a robust and flexible approach to concurrent programming, and mastering the use of actors is a valuable skill for building scalable and fault-tolerant systems.

## Bibliography

1. Official Scala Documentation: https://docs.scala-lang.org/overviews/scala-book/actors.html
2. Akka Documentation: https://doc.akka.io/docs/akka/current/typed/actors.html
3. Lightbend: https://www.lightbend.com/akka-quickstart-scala
4. Baeldung: https://www.baeldung.com/scala/actors
5. Scala Exercises: https://www.scala-exercises.org/scala_tutorial/concurrency_actors
6. Slick tables https://www.baeldung.com/scala/slick-intro

