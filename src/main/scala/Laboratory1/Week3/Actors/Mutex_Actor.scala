package Week3.Actors

import akka.actor.Actor

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
