package com.despegar.indec.hangar.actions

import com.despegar.vr.commons.actions.ActionBuilder
import play.api.mvc.{Request => PlayRequest}
import scaldi.{Injectable, Injector}

class RouterRequestActionBuilder(implicit inj: Injector) extends ActionBuilder[RouterRequest]
                                                               with Injectable {

  override def build[A](request: PlayRequest[A]) = RouterRequest(request.headers.toSimpleMap, request.body)

}
