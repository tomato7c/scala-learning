package xhxc.fp.tool

import java.io.File

object common {

  val set = collection.mutable.Set[String]()

  val consumer: File => Unit = f => {
    if (f.getName.endsWith("java") || f.getName.endsWith("kt")) {
      val size = lineList(f.getAbsolutePath)
        .filter(_.startsWith("import"))
        .filter(_.contains("order"))
        .size
      if (size > 0) {
        set += f.getAbsolutePath
      }
    }
  }

  def main(args: Array[String]): Unit = {
  }

  // consume each file
  def scanFiles(root: String, consumer: File => Unit): Unit = {
    val file = new File(root)
    if (file.isDirectory) {
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
