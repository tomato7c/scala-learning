package xhxc.fp.part3

sealed class Tree[+A] {
  // 3.25
  def size(): Int = this match {
    case Leaf(_) => 1
    case Branch(l, r) => l.size() + 1 + r.size()
  }
  // 3.27
  def depth(): Int = this match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + (l.depth() max r.depth())
  }
  // 3.28
  def map[B](f: A => B): Tree[B] = this match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l, r) => Branch(l.map(f), r.map(f))
  }

  // 3.29
  def fold[B](f: (B, B) => B, g: A => B): B = this match {
    case Leaf(v) => g(v)
    case Branch(l, r) => f(l.fold(f, g), r.fold(f, g))
  }
  def sizeViaFold(): Int = fold[Int]((a, b) => 1 + a + b, _ => 1)
  def depthViaFold(): Int = fold[Int]((a, b) => 1 + (a max b), _ => 1)
  def mapViaFold[B](f: A => B): Tree[B] = fold[Tree[B]](Branch(_, _), a => Leaf(f(a)))

}
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  // 3.26
  def maximum(root: Tree[Int]): Int = root match {
    case Leaf(v) => v
    case Branch(l, r) => maximum(l) max maximum(r)
  }

  def maximumViaFold(root: Tree[Int]): Int = root.fold[Int](_ max _, identity)

  def main(args: Array[String]): Unit = {
    val tree = Branch(Leaf(1), Branch(Leaf(1), Leaf(7)))
  }
}
