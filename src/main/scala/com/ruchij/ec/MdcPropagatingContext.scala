package com.ruchij.ec

import java.util

import org.slf4j.MDC

import scala.concurrent.ExecutionContext

trait MdcPropagatingContext extends ExecutionContext
{
  self =>

  override def prepare(): ExecutionContext = new ExecutionContext {

    val context: util.Map[String, String] = MDC.getCopyOfContextMap

    override def execute(runnable: Runnable): Unit =
      self.execute(() => {
        setContext(context)
        runnable.run()
      })

    override def reportFailure(cause: Throwable): Unit = self.reportFailure(cause)

    def setContext(context: util.Map[String, String]): Unit =
      if (context != null) { MDC.setContextMap(context) } else { MDC.clear() }
  }
}
