package xhxc.fp.part6

/**
 * 用函数式实现带状态的API，原来就是他妈的把状态通过参数传递
 * 从这可以看出与 OOP 的不同
 */
trait RNG {
  def nextInt(): (Int, RNG)
}

object RNG {
  case class Simple(seed: Long) extends RNG {
    override def nextInt(): (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = Simple(newSeed)
      val n = (newSeed >>> 16).toInt
      (n, nextRNG)
    }
  }

  // 6.1
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt()
    val res = if (i < 0) -(i + 1) else i
    (i, r)
  }
  // 6.2
  def double(rng: RNG): (Double, RNG) = {
    val (random, r) = nonNegativeInt(rng)
    val res = random.toDouble / (Int.MaxValue.toDouble + 1)
    (res, r)
  }
  // 6.3
  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (first, r1) = rng.nextInt()
    val (second, r2) = double(rng)
    ((first, second), r2)
  }
  // 6.3
  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val (first, r1) = double(rng)
    val (second, r2) = r1.nextInt()
    ((first, second), r2)
  }
  // 6.3
  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (first, r1) = double(rng)
    val (second, r2) = double(r1)
    val (third, r3) = double(r2)
    ((first, second, third), r3)
  }

  // 6.4
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    if (count <= 0) (List(), rng)
    else {
      val (random, r) = rng.nextInt()
      val (randoms, r2) = ints(count - 1)(r)
      (random :: randoms, r2)
    }
  }

  // 6.5
  type Rand[+A] = RNG => (A, RNG)
  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (value, rng2) = s(rng)
      (f(value), rng2)
    }
  def mapDouble(): Rand[Double] =
    map(nonNegativeInt)(it => it / (Int.MaxValue.toDouble + 1))

  // 6.6 组合两个行为并返回一个新行为
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    rng => {
      val (a, rng1) = ra(rng)
      val (b, rng2) = rb(rng1)
      (f(a, b), rng2)
    }

  // use map2 to simplify, (_, _) is a constructor of tuple2
  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = map2(ra, rb)((_, _))


  type State[S, +A] = S => (A, S)
}

object State {

}

object Test {
  def main(args: Array[String]): Unit = {

    println(Int.MaxValue % 12)
  }
}
