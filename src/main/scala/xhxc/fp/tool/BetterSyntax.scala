package xhxc.fp.tool

object BetterSyntax {

  def main(args: Array[String]): Unit = {
    println("".toCamel())
  }

  implicit class StringSyntax(v: String) {
    def toSnake(): String = {
      def go(s: String): String = s.headOption match {
        case Some(c) if c.isUpper => "_" + c.toLower + go(s.tail)
        case Some(c) => c + go(s.tail)
        case None => ""
      }
      go(v)
    }

    def toCamel(): String = {
      def go(s: String): String = s.headOption match {
        case Some('_') => s.tail.head.toUpper + go(s.tail.tail)
        case Some(it) => it + go(s.tail)
        case None => ""
      }
      go(v)
    }
  }
}
