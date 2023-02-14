package Week2
import com.sun.tools.attach.VirtualMachine.list

import scala.:+
import scala.util.control.Breaks.break
import scala.collection.immutable
import scala.collection.mutable.ListBuffer
import scala.util.Random
object MinimalTasks extends App{
  {
    cylinderArea(4,3)
    isPrime(13)
    reverse(List(1, 2, 4, 8, 4))
    uniqueSum(List(1 , 2 , 4 , 8 , 4 , 2))
    extractRandomN(List(1, 2, 4, 8 , 4), 3)
    firstFibonacciElements(7)
  }

  def isPrime(n: Int): Boolean = {
    var Med: Int = n / 2
    var Flag: Int = 0

    if (n <= 1)
      return false
    else for (i <- 2 to Med) {
      if (n % i == 0) {
        Flag = 1
        break
      }
    }
  if (Flag == 0) {
   println("True")
    return true
  } else {
    println("False")
    return false
  }
  }

  def cylinderArea(r: Double, h: Double): Double = {
    val Pi: Double = Math.PI
    var area = 2 * Pi * r * h + 2 * Pi * scala.math.pow(r, 2)
    println(f"%%.4f".format(area))
    return area
  }

  def reverse(list: List[Int]): Unit = {
    println(list.reverse)
  }

 def uniqueSum(list: List[Int]): Unit = {
  var sum : Int = 0
  val uniqueList = list.to(collection.mutable.Set)
  uniqueList.foreach(sum+= _)
  println(sum)
 }

  def extractRandomN(list: List[Int], Nr: Int) : Unit = {
    var rand = new Random
    var randList = ListBuffer[Int](Nr)
    var randNr : Int = 0
    for(i <- 0 to Nr-1){
      randNr = list(rand.nextInt(Nr))
      while (randList.contains(randNr)){
        randNr = list(rand.nextInt(Nr))
      }
      randList += randNr
    }
    println(randList)
  }

  def firstFibonacciElements(n : Int) : Unit ={
    var fib = Array[Int](0, 1)
    val result = n match{
      case 1 => print(fib(0))
      case 2 => print(fib.mkString(", "))
      case _ => {
        for(i <- 2 until n){
          fib = fib :+ fib(i-1) + fib(i-2)
        }
        print(fib.mkString(", "))
      }
    }
  }
}