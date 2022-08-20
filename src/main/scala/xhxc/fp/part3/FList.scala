package xhx.fp.part3

import scala.annotation.tailrec

/**
 * FList有两种实现(或两个构造器)，对应到两个case class
 * 使用协变符号+后，可以让HList[Nothing]成为任何HList的子类型，因为Nothing是任何对象的子类型
 * A* 是List字面量，它就是表示一个List
 */
sealed trait FList[+A] {

}

case object FNil extends FList[Nothing]
case class Cons[+A](head: A, tail: FList[A]) extends FList[A]

object FList {
  def sum(ints: FList[Int]): Int = ints match {
    case FNil => 0
    case Cons(x, xs) => x + sum(xs)
  }
  def product(ds: FList[Double]): Double = ds match {
    case FNil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }
  def apply[A](as: A*): FList[A] =
    if (as.isEmpty) FNil
    else Cons(as.head, apply(as.tail: _*))
  // 3.2
  def tail[A](l: FList[A]): FList[A] = l match {
    case FNil => throw new IllegalArgumentException()
    case Cons(_, t) => t
  }
  // 3.3
  def setHead[A](l: FList[A], h: A): FList[A] = l match {
    case FNil => Cons(h, FNil)
    case Cons(_, t) => Cons(h, t)
  }
  // 3.4
  def drop[A](l: FList[A], n: Int): FList[A] = {
    if (n == 0) l
    else l match {
      case FNil => FNil
      case Cons(_, t) => drop(t, n - 1)
    }
  }
  // 3.5
  def dropWhile[A](l: FList[A], f: A => Boolean): FList[A] = {
    l match {
      case FNil => FNil
      case Cons(x, t) if f(x) => dropWhile(t, f)
      case _ => l
    }
  }
  // 3.9
  def foldRight[A, B](l: FList[A], res: B)(f: (A, B) => B): B = l match {
    case FNil => res
    case Cons(x, xs) => f(x, foldRight(xs, res)(f))
  }
  // 3.10
  def foldLeft[A, B](l: FList[A], res: B)(f: (A, B) => B): B = l match {
    case FNil => res
    case Cons(x, xs) => foldLeft(xs,f(x, res))(f)
  }
  // 3.11
  def sum2(l: FList[Int]): Int = foldLeft[Int, Int](l, 0)(_ + _)
  def product(l: FList[Int]): Int = foldLeft[Int, Int](l, 1)(_ * _)
  def length2[A](l: FList[A]): Int = foldLeft[A, Int](l, 0)((_, res) => res + 1)
  // 3.12
  def reverse[A](l: FList[A]): FList[A] = foldLeft[A, FList[A]](l, FNil)((it, res) => Cons(it, res))
  // 3.14 in fact, this is fold the right to the tail of left
  def append[A](l: FList[A], r: FList[A]): FList[A] = foldLeft(l, r)(Cons(_, _))

  def flat[A](ll: FList[FList[A]]): FList[A] = foldLeft(ll, FNil: FList[A])((h, t) => append(h, t))
  def flatUseFoldRight[A](ll: FList[FList[A]]): FList[A] = foldRight(ll, FNil: FList[A])((h, t) => append(h, t))

  // 3.16
  def plusOne(l: FList[Int]): FList[Int] = foldRight(l, FNil: FList[Int])((h, t) => Cons(h + 1, t))
  // 3.17
  def mapToString(l: FList[Double]): FList[String] = foldRight(l, FNil: FList[String])((h, t) => Cons(h.toString, t))
  // 3.18
  def map[A, B](l: FList[A])(f: A => B): FList[B] = foldRight(l, FNil: FList[B])((h, t) => Cons(f(h), t))
  // 3.19
  def filter[A](l: FList[A])(f: A => Boolean): FList[A] = foldRight(l, FNil: FList[A])((h, t) => if(f(h)) Cons(h, t) else t)
  // 3.20
  def flatMap[A, B](l: FList[A])(f: A => FList[B]): FList[B] = flatUseFoldRight(map(l)(f))
  // 3.21
  def filterViaFlatMap[A](l: FList[A])(f: A => Boolean): FList[A] = flatMap(l)(x => if(f(x)) FList(x) else FNil)
  // 3.22
  def zip(l1: FList[Int], l2: FList[Int]): FList[Int] = (l1, l2) match {
    case (FNil, _) => FNil
    case (_, FNil) => FNil
    case (Cons(h1, t1), Cons(h2, t2)) => Cons(h1 + h2, zip(t1, t2))
  }
  // 3.23
  def zipWith[A](l1: FList[A], l2: FList[A])(combine: (A, A) => A): FList[A] = (l1, l2) match {
    case (FNil, _) => FNil
    case (_, FNil) => FNil
    case (Cons(h1, t1), Cons(h2, t2)) => Cons(combine(h1, h2), zipWith(t1, t2)(combine))
  }
  // 3.24
  @tailrec
  def startsWithSeq[A](sup: FList[A], sub: FList[A]): Boolean = (sup, sub) match {
    case (_, FNil) => true
    case (FNil, _) => false
    case (Cons(h1, t1), Cons(h2, t2)) if h1 == h2 => startsWithSeq(t1, t2)
    case _ => false
  }
  @tailrec
  def hasSubsequence[A](sup: FList[A], sub: FList[A]): Boolean = sup match {
    case FNil => sub == FNil
    case _ if startsWithSeq(sup, sub) => true
    case Cons(_, t) => hasSubsequence(t, sub)
  }
}

object Application {
  def main(args: Array[String]): Unit = {
    val l1 = FList(1, 2, 3, 4, 5)
    val l2 = FList(3, 4, 5, 6)
    println(FList.hasSubsequence(l1, l2))
  }
}
