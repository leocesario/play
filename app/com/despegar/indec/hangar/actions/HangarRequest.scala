package com.despegar.indec.hangar.actions

import com.despegar.vr.commons.actions.{Headers, Request}

case class HangarRequest[T](headers: Map[String, String],
                        content: T) extends Request[T]
                                      with Context

trait Context extends Headers
