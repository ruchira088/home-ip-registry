package com.ruchij.ec

import scala.concurrent.ExecutionContext

class DefaultMdcPropagatingContext(executionContext: ExecutionContext) extends MdcPropagatingContext
{
  override def execute(runnable: Runnable): Unit = executionContext.execute(runnable)

  override def reportFailure(cause: Throwable): Unit = executionContext.reportFailure(cause)
}

object DefaultMdcPropagatingContext
{
  def apply(executionContext: ExecutionContext): DefaultMdcPropagatingContext =
    new DefaultMdcPropagatingContext(executionContext)
}