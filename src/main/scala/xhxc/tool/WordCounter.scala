package xhxc.tool

object WordCounter {
  def main(args: Array[String]): Unit = {
    def printWords(w: String, f: Int): Unit = {
      val blackList = List("or", "the", "a", "that", "for", "on", "in", "out", "can", "of", "to", "and", "is",
      "as", "an", "it", "are", "with", "this", "we", "the", "but", "have", "i", "has", "they", "be", "you", "your")
      if (!blackList.contains(w.toLowerCase)) println(s"$w: $f")
    }

    val group = common.lineList("/Users/xuhongxu/Desktop/temp1")
      .filter(!_.isBlank)
      .flatMap(_.split("\\W+"))
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toList.sortBy(_._2)
    var count = 0
    for ((k, v) <- group) {
      printWords(k, v)
      count += v
    }
    println (s"total: $count")


//    val sum = common.lineList("/Users/xuhongxu/Desktop/temp1")
//      .filter(!_.isBlank)
//      .map(_.trim)
//      .map(_.split(" ").length)
//      .sum
//    println(sum)
  }
}
