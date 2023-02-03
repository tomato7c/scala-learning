package xhxc.tool

import java.io.File

object common {

  val set = collection.mutable.Set[String]()
  var total = 0

  val consumer: File => Unit = f => {
    val fname = f.getName
    if (!fname.endsWith("Test.java") && (fname.endsWith(".java") || fname.endsWith(".kt"))) {
      val size = lineList(f.getAbsolutePath)
        .map(_.trim)
        .filter(!_.startsWith("import"))
        .filter(!_.isBlank)
        .filter(!_.startsWith("//"))
        .filter(!_.startsWith("/**"))
        .filter(!_.startsWith("*"))
        .filter(!_.startsWith("*/"))
        .filter(!_.startsWith("/*"))
        .size
      if (size > 0) {
        println(s"$fname: $size lines")
        total += size
      }
    }
  }

  def main(args: Array[String]): Unit = {
    scanFiles("", consumer)
    println(s"total $total lines")
  }

  // consume each file
  def scanFiles(root: String, consumer: File => Unit): Unit = {
    val file = new File(root)
    if (file.isDirectory) {
      if (file.getName.equals("target")) return

      for (it <- file.listFiles) {
        scanFiles(it.getAbsolutePath, consumer)
      }
    } else {
      consumer(file)
    }
  }

  def diff(fileA: String, fileB: String): Unit = {
    val listA = lineList(fileA)
    val listB = lineList(fileB)

    listA.filter(!listB.contains(_)).foreach(it => println(s"$fileA 多出 $it"))
  }

  def lineList(file: String): List[String] = {
    val source = scala.io.Source.fromFile(file)

    val list = try {
      source.getLines().toList
    } finally {
      source.close()
    }
    list
  }
}
