package xhxc.fp.part6

object Part6 {
  def main(args: Array[String]): Unit = {
    println(Int.MinValue)
  }

  private def repeat(times: Int)(func: => Unit): Unit = {
    for (_ <- 1 to times) {
      func
    }
  }
}
