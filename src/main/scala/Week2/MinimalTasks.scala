package Week2
import com.sun.tools.attach.VirtualMachine.list
import scala.:+
import scala.util.control.Breaks.break
import scala.collection.immutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random
object MinimalTasks extends App {
  {
    isPrime(13)
    cylinderArea(4, 3)
    reverse(List(1, 2, 4, 8, 4))
    uniqueSum(List(1, 2, 4, 8, 4, 2))
    extractRandomN(List(1, 2, 4, 8, 4), 3)
    firstFibonacciElements(7)
    dictionary(Map("mama" -> "mother", "papa" -> "father"), "mama is with papa")
    smallestNumber(ArrayBuffer(4,5,3))
    smallestNumber(ArrayBuffer(0, 3, 4))
    rotateLeft(List(1, 2, 4, 8 ,4), 3)
    listRightAngleTriangles()
  }

  def isPrime(n: Int): Boolean = {
    println("Minimal Task 1")
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
    println("Minimal Task 2")
    val Pi: Double = Math.PI
    var area = 2 * Pi * r * h + 2 * Pi * scala.math.pow(r, 2)
    println(f"%%.4f".format(area))
    return area
  }

  def reverse(list: List[Int]): Unit = {
    println("Minimal Task 3")
    println(list.reverse.mkString(", "))
  }

  def uniqueSum(list: List[Int]): Unit = {
    println("Minimal Task 4")
    var sum: Int = 0
    val uniqueList = list.to(collection.mutable.Set)
    uniqueList.foreach(sum += _)
    println(sum)
  }

  def extractRandomN(list: List[Int], Nr: Int): Unit = {
    println("Minimal Task 5")
    var rand = new Random
    var randList = ListBuffer[Int](Nr)
    var randNr: Int = 0
    for (i <- 0 to Nr - 1) {
      randNr = list(rand.nextInt(Nr))
      while (randList.contains(randNr)) {
        randNr = list(rand.nextInt(Nr))
      }
      randList += randNr
    }
    println(randList.mkString(", "))
  }

  def firstFibonacciElements(n: Int): Unit = {
    println("Minimal Task 6")
    var fib = Array[Int](0, 1)
    val result = n match {
      case 1 => print(fib(0))
      case 2 => print(fib.mkString(", "))
      case _ => {
        for (i <- 2 until n) {
          fib = fib :+ fib(i - 1) + fib(i - 2)
        }
        print(fib.mkString(", "))
      }
    }
  }

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

  def smallestNumber(input: ArrayBuffer[Int]): Unit = {
    println("Minimal Task 8")
    var result = input
    if (!input.contains(0)) {
      result = input.sorted
      println(result.mkString(""))
    }
    else {
      var temp = new Array[Int](3)
      val output = (input.count(_ == 0)) match {
        case 1 => {
          temp(1) = result.min
          result -= result.min
          temp(0) = result.min
          result -= result.min
          temp(2) = result.min
          println(temp.mkString(""))
        }
        case 2 => {
          temp(1) = result.min
          temp(2) = result.min
          temp(0) = result.max
          println(temp.mkString(""))
        }
        case 3 => println(result.mkString(""))
      }
    }
  }
  def rotateLeft(list: List[Int], n: Int): Unit = {
    println("Minimal Task 9")
    var output = new ListBuffer[Int]()
    val(firsthalf, secondhalf) = list.splitAt(n)
    println((secondhalf++firsthalf).mkString(""))
  }

  def listRightAngleTriangles(): Unit = {
    println("Minimal Task 10")
    for (
      a <- 1 to 20 by 1;
      b <- a to 20 by 1
    ) {
      val c = math.sqrt(a * a + b * b).toInt
      if (a * a + b * b == c * c) {
        println(s"$a $b $c")
      }
    }
  }

}