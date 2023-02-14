import Week2.MinimalTasks
import org.scalatest.funsuite.AnyFunSuite

class Test extends AnyFunSuite {
    val minimalFunctions = new MinimalTasks

  test("Testing main class for Week1") {
    assert(minimalFunctions.isPrime(3) == true)
  }
}
