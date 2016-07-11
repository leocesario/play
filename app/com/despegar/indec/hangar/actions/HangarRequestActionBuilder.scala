package com.despegar.indec.hangar.actions

import com.despegar.vr.commons.actions.ActionBuilder
import play.api.mvc.{Request => PlayRequest}
import scaldi.{Injectable, Injector}

class HangarRequestActionBuilder(implicit inj: Injector) extends ActionBuilder[HangarRequest]
                                                               with Injectable {

  override def build[A](request: PlayRequest[A]) = HangarRequest(request.headers.toSimpleMap, request.body)

}
