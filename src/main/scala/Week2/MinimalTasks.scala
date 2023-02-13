import com.sun.tools.attach.VirtualMachine.list

import scala.util.control.Breaks.break
import scala.collection.immutable
object Task2 extends App{
  {
    cylinderArea(4,3)
    isPrime(13)
    val list = List(1, 2, 4, 8, 4)
    reverse(list)
  }

  def isPrime(n: Int): Boolean = {
    var Med: Int = n / 2
    // var Counter: Int = 2
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
   println(n+ "is Prime")
    return true
  } else {
    println(n + "is not Prime")
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
}