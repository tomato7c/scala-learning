package xhxc.fp.tool.test

object Tests {
  def currentMethodName(): String = Thread.currentThread().getStackTrace()(2).getMethodName
}
