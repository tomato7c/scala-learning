package xhxc.fp.tool

import java.io.File

import scala.io.Source

object FileHelper {

  private val res = collection.mutable.ListBuffer[String]()

  private var count = 0

  def main(args: Array[String]): Unit = {

  }

  /**
   * 通过java、kt文件import查找某个类在指定目录下的所有调用点
   * @param target 目标类名
   * @param file 要查找的目录/文件
   */
  def scanFiles(target: String, file: File): Unit = {
    if (file.isDirectory) {
      for (it <- file.listFiles()) scanFiles(target, it)
    } else {
      findImport(target, file)
    }
  }

  def findImport(target: String, file: File): Unit = {
    if (!file.getName.endsWith(".java") && !file.getName.endsWith(".kt")) return
    count += 1
    val source = Source.fromFile(file)
    val opt = source.getLines().toStream.find(_.contains(target))
    if (opt.isDefined) {
      res += file.getName
    }
    source.close()
  }
}
