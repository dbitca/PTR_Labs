import scala.Console.println

object MainTasks extends App {
{
  removeConsecutiveDuplicates(List(1 , 2, 2, 2, 4, 8, 4))
  lineWords(List("qtwue","siwd"))
}

def removeConsecutiveDuplicates(list : List[Int]): Unit = {
  print(list(0) + " ")
  for( i <- 1 until list.length){
    if(list(i) != list(i -1))
      print(list(i) + " ")
  }
  println
}

def lineWords(list: List[String]): Unit = {
  val rowOne : List[Char] = List('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p')
  val rowTwo : List[Char] = List('a', 's', 'd', 'f', 'g', 'h', 'j','k', 'l')
  val rowThree: List[Char] = List('z', 'x', 'c', 'v', 'b', 'n', 'm')

  list.foreach(s =>{
    if(s.forall(c => rowOne.contains(c))) print(s)
    else if(s.forall(c => rowTwo.contains(c))) print(s)
    else if(s.forall(c => rowThree.contains(c))) print(s)
  })
}
  def groupAnagrams(list: List[String]): Unit = {

  }
}
