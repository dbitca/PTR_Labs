import Week2.MinimalTasks
import org.scalatest.funsuite.AnyFunSuite
import Week1.HelloPtr

class Test extends AnyFunSuite {
  test("Testing HelloPtr class for Week1") {
    assert(HelloPtr.sayHello("Hello PTR!") == "Hello PTR!")
  }
}
