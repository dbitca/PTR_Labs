import scala.Console.println

object MainTasks extends App {
  {
    removeConsecutiveDuplicates(List(1, 2, 2, 2, 4, 8, 4))
    lineWords(List("Hello", "Alaska", "Dad", "Peace"))
    groupAnagrams(List("eat", "tea", "tan", "ate", "nat", "bat"))
  }

  def removeConsecutiveDuplicates(list: List[Int]): Unit = {
    print(list(0) + " ")
    for (i <- 1 until list.length) {
      if (list(i) != list(i - 1))
        print(list(i) + " ")
    }
    println
  }

  def lineWords(list: List[String]): Unit = {

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

  def groupAnagrams(list: List[String]): Unit = {
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
