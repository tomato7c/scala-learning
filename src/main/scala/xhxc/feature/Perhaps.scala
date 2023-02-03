package xhxc.feature

sealed abstract class Perhaps[+A] {
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): Perhaps[B]
  def flatMap[B](f: A => Perhaps[B]): Perhaps[B]
  def withFilter(f: A => Boolean): Perhaps[A]
}

case class YesItIs[A](value: A) extends Perhaps[A] {
  override def foreach(f: A => Unit): Unit = f(value)
  override def map[B](f: A => B): Perhaps[B] = YesItIs(f(value))
  override def flatMap[B](f: A => Perhaps[B]): Perhaps[B] = f(value)
  override def withFilter(f: A => Boolean): Perhaps[A] = if(f(value)) this else Nope
}

/**
 * Nothing is at the bottom of type system
 * So Nope is a value of any type Perhaps[A]
 *
 * [+A] 表示如果Nothing是B的subtype, 那么Perhaps[Nothing]是Perhaps[B]的subtype
 * 这就代表Nope可以用在任何 require Perhaps[xxx]的地方
 */
case object Nope extends Perhaps[Nothing] {
  override def foreach(f: Nothing => Unit): Unit = ()
  override def map[B](f: Nothing => B): Perhaps[B] = this
  override def flatMap[B](f: Nothing => Perhaps[B]): Perhaps[B] = this
  override def withFilter(f: Nothing => Boolean): Perhaps[Nothing] = this
}

/**
 * for-loop是命令式的
 */
object Perhaps {
  def main(args: Array[String]): Unit = {
    val y3 = YesItIs(3)
    val y4 = YesItIs(4)
    val n = Nope

    val res = for {
      a <- y3
      if a > 10
    } yield a
    println(res)
  }
}

