package xhxc.fp.part7

import java.util.concurrent.{ExecutorService, Future, TimeUnit}

/**
 * 什么样的数据类型和函数能够将计算并行化？
 * 什么是一个理想的 API?（用来支持并行计算）
 * 1. client 感知不到 Thread 的存在
 * 2. 资源安全
 * 3. client 需要确定如何拆分任务，如何执行任务，如何组合结果
 * 4. 数据类型和函数上有什么帮助，老子不理解
 */
object Par {
  // Par[A] 是一个函数类型
  type Par[A] = ExecutorService => Future[A]

  private case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false

    override def isCancelled: Boolean = false

    override def isDone: Boolean = true

    override def get(timeout: Long, unit: TimeUnit): A = get
  }

  def unit[A](a: A): Par[A] = _ => UnitFuture(a)

  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    es => {
      val fa = a(es)
      val fb = b(es)
      UnitFuture(f(fa.get, fb.get))
    }
/**
 * unit 和 get 这两个 api 的缺陷
 * 1. 如果运算发生在 unit, 那个 get 会阻塞主线程来获取结果，这两个 api 的使用不符合引用透明
 * 2. 如果运算的触发推迟到 get，并不会有真正的并行
 */

object Test {
  def main(args: Array[String]): Unit = {

  }

  def sum(ints: Seq[Int]): Int = ints.foldLeft(0)(_ + _)

  def sum(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption.getOrElse(0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      sum(l) + sum(r)
    }
  }
}
