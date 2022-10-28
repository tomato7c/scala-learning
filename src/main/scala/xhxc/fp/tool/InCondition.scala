package xhxc.fp.tool

object InCondition {
  def main(args: Array[String]): Unit = {

  }

  def printInCondition(file: String): Unit = {
    var list = lineList(file)
    println(list.size)
    list = list.map(str => s"'${str.trim}'")
    println(s"(${list.mkString(",")})")
  }

  def lineList(file: String): List[String] = {
    val source = scala.io.Source.fromFile(file)

    val list = try {
      source.getLines().toList
    } finally {
      source.close()
    }
    list
  }
}
