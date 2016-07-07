package com.despegar.indec.hangar.actions

import com.despegar.vr.commons.actions.{Automation, Headers, Request, RequestAndResponseBodiesLogging}
import com.despegar.vr.commons.cache.context.Cache

case class RouterRequest[T](headers: Map[String, String],
                        content: T) extends Request[T]
                                      with Context

trait Context extends Headers
                        with Automation
                        with RequestAndResponseBodiesLogging
                        with Cache