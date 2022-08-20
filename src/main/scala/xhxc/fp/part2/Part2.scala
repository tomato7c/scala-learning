package xhxc.fp.part2

class Exercise2 {
  // 练习 2.1 熟悉如何通过递归来表达循环
  def fib(n: Int): Int = {
    def go(n: Int, a: Int, b: Int): Int = {
      if (n == 0) a
      else go(n - 1, a + b, a)
    }
    go(n, 0, 1)
  }

  // 多态函数
  def partial1[A, B, C](a: A, f: (A, B) => C): B => C = b => f(a, b)
  // 练习 2.3
  def curry[A, B, C](f: (A, B) => C): A => (B => C) = a => b => f(a, b)
  // 练习 2.4
  def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)
  // 练习 2.5
  def compose[A, B, C](f: B => C, g: A => B): A => C = a => f(g(a))
}
