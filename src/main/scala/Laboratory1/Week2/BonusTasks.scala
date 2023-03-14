import scala.util.control.Breaks.break

object  BonusTasks extends App {
  {
  commonPrefix(List("flower", "flow", "flight"))
  commonPrefix(List("alpha", "beta", "gama"))
   factorize(13)
   factorize(42)
  }

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
  def factorize(nr : Int): Unit = {
    println("Bonus Task 2")
    var factors = List[Int]()
    var  n = nr
    for (i <- 2 to n) {
      while (n % i == 0) {
        factors = factors.appended(i)
        n = n / i
      }
    }
      for (f <- factors) {
        print(f + " ")
      }
    println
  }
  }
