# FAF.PTR16.1 -- Project 0
> **Performed by:** Bitca Dina, group FAF-201
> **Verified by:** asist. univ. Alexandru Osadcenco

## P0W1
The first week's laboratory work consisted of two minimal tasks, one main task, and two bonus task. In the scope of 
this report I will only be explaining the tasks that are 

**Task 1**

The minimal task consisted of writing a script that would print the message "Hello PTR!". In order to be able to 
create a unit testing of the code, I have created a function that would print the message given as an argument. The code for the Minimal Task is represented in the code snippet below:

```scala
object HelloPtr extends App {
  {
    sayHello("Hello PTR!")
  }
  def sayHello (message: String): String = {
    print(message)
    return message
  }}
```
As mentioned above, I have defined a "sayHello" function that gets the message "Hello PTR" as argument and prints the message on the console using the print method. This function is then called in the "HelloPtr" object which extends the "App" trait. The "App" trait is used to create Scala applications that can be run as standalone programs.

**Task 2**

The code snippet for the unit test of the Minimal Task is described in the code snippet below.
```scala
class Test extends AnyFunSuite {
  test("Testing HelloPtr class for Week1") {
    assert(HelloPtr.sayHello("Hello PTR!") == "Hello PTR!")
  }
}
```
As it can be seen, the test class extends the "AnyFunSuite" trait which is used to define tests in ScalaTest. Inside the class there is a single test function named "Testing HelloPtr class for Week1". The body of the test function uses the "assert" method to verify that the result of calling the "sayHello" method on the "HelloPtr" object, with the argument "Hello PTR!", is equal to the string "Hello PTR!". If the assertion fails, the test displays an error message on the console.

## P0W2
This week's laboratory work consisted of ten Minimal Tasks, five Main Tasks and three Bonus tasks. To exemplify my
work for this laboratory, I will be describing one of each tasks, as well as my logic of implementation and code
snippets.

**Task 1** 

For the first task, I will be exemplifying one of the minimal tasks that have been successfully solved. The task was 
to write a function that, given a dictionary, would translate a sentence. Words
not found in the dictionary need not be translated. 
The result for the solved task can be seen in the code snippet below:

```scala
 def dictionary(dictionary: Map[String, String], text: String): Unit = {
    println("Minimal Task 7")
    var translated = text

    dictionary.keys.foreach(key => {
      if (text.contains(key)) {
        translated = translated.replace(key, dictionary(key))
      }
    })
    println(translated)
  }
```
This code defines a function called "dictionary" that takes two arguments: a Map of String key-value pairs representing a dictionary, and a String called "text". The function's purpose is to search for occurrences of any keys in the dictionary in the text argument and replace them with their corresponding values in the dictionary.
The function iterates through each key in the dictionary using the "foreach" method of the keys collection. For each key, 
the 
function checks if it appears in the text argument using the "contains" method of String. If the key is found in the text, the function uses the replace method of String to replace all occurrences of the key in translated with its corresponding value in the dictionary.
Finally, the function prints the updated value of translated to the console.

**Task 2** 

For the second task, I will be exemplifying one of the main tasks given. The tasks was to write a function that, 
given an array of strings, will return the words that can
be typed using only one row of the letters on an English keyboard layout.
The code snippet for the solved task can be observed below:

```scala
  def lineWords(list: List[String]): Unit = {
  println("Main Task 2")
  val rowOne: List[Char] = List('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p')
  val rowTwo: List[Char] = List('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l')
  val rowThree: List[Char] = List('z', 'x', 'c', 'v', 'b', 'n', 'm')

  list.foreach(s => {
    if (s.forall(c => rowOne.contains(c.toLower))) print(s + " ")
    else if (s.forall(c => rowTwo.contains(c.toLower))) print(s + " ")
    else if (s.forall(c => rowThree.contains(c.toLower))) print(s + " ")
  })
  println
}
```
This function's purpose is to print out all words in the input list that can be typed using only one row of a 
standard keyboard.
The function initializes three variables "rowOne", "rowTwo", and "rowThree", which are each a List of Chars representing the keys in the top, middle, and bottom rows of a standard keyboard, respectively

Next, the function iterates through each string "s" in the input list using the foreach method. For each string "s", the function checks whether all of its characters can be found in one of the keyboard rows. This is done using the "forall" method of String, which checks whether a given predicate holds true for every element of the string.

If "s" contains only characters from the first row of the keyboard, the function prints the string followed by a space using the print method. If "s" contains only characters from the second or third row, the function prints the string followed by a space using the print method. Note that the toLower method is called on each character in s to ensure that the function correctly matches uppercase and lowercase characters.

**Task 3**
For the third task, I will be exemplifying one of the bonus tasks given in the scope of this laboratory work. The 
task was to write a function to find the longest common prefix string amongst a list of
strings.

```scala
  def commonPrefix(list: List[String]): Unit = {
    println("Bonus Task 1")
    var temp = ""
    var i = 0
    var flag = true
    while (flag) {
      temp = temp + list(0)(i)
      if (list.forall(s => s.startsWith(temp)))
        i = i + 1
      else {
        temp = temp.dropRight(1)
        flag = false
      }
    }
    println(list.mkString(", ") + " -> " + temp)
  }
```

This function initializes three variables "temp", "i", and "flag". "temp" is a String variable that stores the common prefix, "i" is an integer variable that tracks the current index of the prefix being checked, and "flag" is a boolean variable that determines when the prefix search is complete.

The function then enters a "while" loop that continues until the flag variable is set to false. Inside the "while" loop, the function appends the character at the current index of the first string in the list to the temp variable. It then checks whether every string in the list starts with "temp" using the "forall" method of "List". If every string in the list starts with "temp", the function increments i by 1 to move on to the next character. If not, the function removes the last character of "temp" using the "dropRight" method of String and sets flag to false to exit the while loop.

## P0W3
The focus of this laboratory work was the creation of actors and supervisors. To reflect my work in the scope of 
this project, I will be exemplifying one task out of each type "Minimal" and "Main".

**Task 1**

For the first minimal task, we had to create an actor that prints on the screen any message it receives. 
Check the code snippet for the actor function below:

```scala
class Actor_1 extends Actor{
  override def receive: Receive = {
    case message => println(message)
  }
}
```
The "Actor_1" class overrides the "receive" method provided by the "Actor" class, which is a message-handling method that is called whenever a message is received by the actor. In this case, the "receive" method simply takes a single parameter called message and prints it to the console using the println method.


When an instance of the Actor_1 class is created and started, it will wait for messages to be sent to it. Whenever a message is sent to the actor, the "receive" method will be called with the message as its parameter. In this case, the message will simply be printed to the console.

The code snippet for the message sending task can be seen below:

```scala
  val system = ActorSystem("my-actors")
  val actor1 = system.actorOf(Props[Actor_1], "actor1")
def Task1(message: String): Unit = {
  actor1 ! message
}
system.terminate()
```

This code first creates an instance of the "ActorSystem" class from the Akka library and assigns it to a variable 
named "system". 

The code then creates an instance of the "Actor_1" class using the "system.actorOf" method and assigns it to a variable named "actor1". The "actorOf" method takes two parameters: the first parameter is a "Props" object that specifies the class of the actor to create (in this case, Actor_1), and the second parameter is a name for the actor (in this case, "actor1").

After creating the actor, the code defines a function called "Task1" that takes a single parameter message and sends it to the actor1 actor using the ! operator. This operator is used to send a message to an actor, and the message can be of any type.

Finally, the code terminates the system by calling its "terminate" method.

**Task 2**

For the main task, we had to create a module that would implement a semaphore. 
The code snippet for the solved task can be observed below:

```scala
class Mutex_Actor extends Actor{
  var semaphore_slots: Int = 0;
  var initial_slots: Int = 0;
  override def receive: Receive = {
    case("create_semaphore", n:Int) =>
      semaphore_slots = n;
      initial_slots = n
    case("acquire", _) =>
      var temp = semaphore_slots;
      if(temp != 0) {
      semaphore_slots -= 1
      println("semaphore slots " + semaphore_slots)
      } else
        println("There are no slots available")
     case("release", _)=>
       if(semaphore_slots < initial_slots ){
       semaphore_slots +=1
       println("semaphore slots " + semaphore_slots)}
      else
      println("Cannot release more slots than the semaphore has")
     }
}
```
This code defines an Akka actor class called "Mutex_Actor". The actor maintains a semaphore with a fixed number of slots and allows other actors to acquire and release slots from the semaphore.

The receive method in this case handles three types of messages:

1. "create_semaphore" with an integer parameter n: This message initializes the semaphore with n slots. The 
semaphore_slots and initial_slots instance variables are set to n.

2. "acquire" with any parameter: This message attempts to acquire a slot from the semaphore. If there is at least one 
slot available (semaphore_slots is greater than 0), the number of available slots is decremented by one and a message is printed to the console indicating the current number of available slots. Otherwise, a message is printed indicating that there are no slots available.

3. "release" with any parameter: This message releases a previously acquired slot back to the semaphore. If the number 
of available slots is less than the initial number of slots (semaphore_slots is less than initial_slots), the number of available slots is incremented by one and a message is printed to the console indicating the current number of available slots. Otherwise, a message is printed indicating that it is not possible to release more slots than the semaphore has.

## P0W4

The task for the fourth week has also been mainly focused on actors and their behaviour. As it has been previously 
stated, I will be exemplifying one of the each types of tasks.

**Task 1**
The minimal task consists of creating a supervised pool of identical worker actors. The number of actors
is static, given at initialization. Workers should be individually addressable. Worker actors
should echo any message they receive. If an actor dies (by receiving a “kill” message), it should
be restarted by the supervisor.
The code snippet for the worker actor can be seen below:

````scala
class Actor_worker extends Actor{
  override def receive: Receive = {
    case message => {
      if(message == "kill") throw new Exception("The worker actor is dead")
      println(s"${self.path.name} received message: $message")
    }
  }
}
````
This code defines an Akka actor class called Actor_worker. The actor listens for messages and processes them 
according to the following logic:

1. If the message is equal to the string "kill", an exception is thrown with the message "The worker actor is dead".
2. Otherwise, the actor prints a message to the console indicating that it has received a message and what that 
   message is.

The code snippet for the supervisor actor can be seen below:

```scala
class Supervisor extends Actor{
  val strategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute){
    case _: Exception => Restart
  }
  override def receive: Receive ={
    case ("CreateWorkers", n: Int) =>
      for (i <- 1 to n){
        val worker = context.actorOf(Props[Actor_worker], s"worker$i")
        context.watch(worker)
      }
      case(message:String, i: Int) => {
        context.child(s"worker$i") match {
          case Some(worker) => worker ! message
          case None => println(s"Worker $i does not exist!")
        }
      }
        case Terminated(actor) =>
        {
          println(s"Actor ${actor.path.name} has died!")
        }
    case _ => {
      println("I don't know what to do with this")
    }}
```
The "receive" method of the "Supervisor" actor handles several types of messages:
1. When receiving a message "CreateWorkers" with an integer value, it creates n child actors of type Actor_worker 
using the context.actorOf() method, and then watches over them using the context.watch() method.
2. When receiving a message of the form (message:String, i: Int), it checks if there is a child actor named worker$i 
   using the context.child() method. If such an actor exists, it sends the message to that child actor using the ! (tell) method. If not, it prints an error message.
3. When receiving a message that a child actor has terminated, it prints a message indicating the name of the actor 
   that has died.
4. When receiving any other message, it prints an error message.

Overall, this code defines a supervisor actor that creates and manages a set of worker actors, and implements a fault-tolerance strategy for those actors.

**Task 2**
The main task consists of creating a supervised processing line to clean messy strings. The first worker in
the line would split the string by any white spaces (similar to Python’s "str.split" method).
The second actor will lowercase all words and swap all m’s and n’s (you nomster!). The third
actor will join back the sentence with one space between words (similar to Python’s "str.join"
method). Each worker will receive as input the previous actor’s output, the last actor printing
the result on screen. If any of the workers die because it encounters an error, the whole
processing line needs to be restarted.

The code snippet for the first actor, called "split-actor" can be seen below:

```scala
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
```
This code does the following:
1. The actor takes an ActorRef reference as its constructor parameter, which is set as the next actor to send the 
processed message to.
2. The "words_list" is an empty list of strings that will hold the words from the incoming message.
3. The "receive" method of the actor defines the message handling behavior of the actor. It expects a String message 
   containing text to split into words. The message is first cleaned up by trimming white spaces and replacing multiple spaces with a single space using the replaceAll method.
4. The split method is then used to split the message into a list of words using the whitespace as the delimiter. The 
   resulting list is stored in the words_list.
Finally, the words_list is sent to the next actor reference using the ! operator.

## Conclusion

In conclusion, working with actors in Scala can be a powerful tool for building concurrent and distributed systems. Actors allow for isolated and independent units of computation that communicate through message passing, providing a more resilient and fault-tolerant system. In this set of examples, we saw how actors can be used for simple tasks like printing messages, to more complex tasks like implementing a mutex and a supervisor for managing worker actors. We also saw how actors can be supervised and restarted in case of failures, adding an extra layer of reliability to the system. Overall, Scala actors offer a robust and flexible approach to concurrent programming, and learning how to use them can be an important skill for building scalable and fault-tolerant systems.

## Bibliography

1. Official Scala Documentation: https://docs.scala-lang.org/overviews/scala-book/actors.html
2. Akka Documentation: https://doc.akka.io/docs/akka/current/typed/actors.html
3. Lightbend: https://www.lightbend.com/akka-quickstart-scala
4. Baeldung: https://www.baeldung.com/scala/actors
5. Scala Exercises: https://www.scala-exercises.org/scala_tutorial/concurrency_actors
