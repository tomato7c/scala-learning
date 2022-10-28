package xhxc.fp.part5

trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]


object Stream {
  def cons[A](h: => A, t: => Stream[A]): Stream[A] = {
    lazy val head = h // 使用 call-by-name + lazy，既避免严格求值，又避免重复求值
    lazy val tail = t
    Cons(() => head, () => tail)
  }
  def empty[A]: Stream[A] = Empty
  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  def main(args: Array[String]): Unit = {
    def what(a: => Int): Option[Int] = {
      Some(a)
    }
    what({
      println("init")
      12
    })
  }
}

