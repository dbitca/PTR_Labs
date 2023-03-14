package Week1
object HelloPtr extends App {
  {
    sayHello("Hello PTR!")
  }
  def sayHello (message: String): String = {
    print(message)
    return message
  }
}
