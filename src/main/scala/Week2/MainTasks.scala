import scala.Console.println

object MainTasks extends App {
  {
    removeConsecutiveDuplicates(List(1, 2, 2, 2, 4, 8, 4))
    lineWords(List("Hello", "Alaska", "Dad", "Peace"))
    encode("lorem", 3)
    decode("oruhp", 3)
    groupAnagrams(List("eat", "tea", "tan", "ate", "nat", "bat"))
  }

  def removeConsecutiveDuplicates(list: List[Int]): Unit = {
    println("Main Task 1")
    print(list(0) + " ")
    for (i <- 1 until list.length) {
      if (list(i) != list(i - 1))
        print(list(i) + " ")
    }
    println
  }

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
  def encode (plaintext: String, key: Int): Unit ={
    println("Main Task 3 -> encode")
    for (c <- plaintext){
      print((c.toInt + key).toChar)
    }
    println()
  }

  def decode (cipher: String, key: Int): Unit ={
    println("Main Task 3 -> decode")
    for (c <- cipher){
      print((c.toInt - key).toChar)
    }
    println
  }
  def groupAnagrams(list: List[String]): Unit = {
    println("Main Task 4")
    var map: Map[String, List[String]] = Map.empty[String, List[String]]

    for (i <- 0 until list.length) {
      var temp = List[String]()
      for (j <- 0 until list.length) {
        if (list(i).sorted == list(j).sorted) {
          temp = temp ++ List(list(j))
        }
      }
      map += (temp(0).sorted -> temp)
    }
    map.foreach { case (key, values) =>
      println(s"$key -> ${values.mkString(", ")}")
    }
  }

}
