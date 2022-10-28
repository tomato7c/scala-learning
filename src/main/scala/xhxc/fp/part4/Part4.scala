package xhxc.fp.part4


object Part4 {
  def main(args: Array[String]): Unit = {
    val 2 = 2
    println(2)
  }

  def get(): Int = {
    try {
      val x = 1
      x + ((throw new Exception("你能咋样？")): Int)
    } catch {
      case e: Exception => 43
    }
  }
}
