package xhxc.tool

object DbConfigChecker {
  def main(args: Array[String]): Unit = {
    check()
  }

  private def check(): Unit = {
    val xml = scala.xml.XML.loadFile("gmetadata/xml.xml")
    val groupList = xml \\ "db_group"
    groupList.foreach(group => {
      val instances = group \ "instance"
      // find writer host
      val writerHost = instances
        .find(_.attribute("type").exists(_.text.equals("writer")))
        .getOrElse(throw new Exception)
        .attribute("host").get
      // find invalid reader host
      val maybeNode = instances
        .filterNot(_.attribute("type").exists(_.text.equals("writer")))
        .find(_.attribute("host").get.equals(writerHost))

      if (maybeNode.isDefined) println(s"invalid host ${writerHost}")
    })
  }
}
