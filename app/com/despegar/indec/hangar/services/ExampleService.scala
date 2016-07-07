package com.despegar.indec.hangar.services

import com.despegar.indec.hangar.actions.Context
import com.despegar.indec.hangar.api.SearchRequest
import com.despegar.indec.hangar.factory.RouterServiceFactory
import com.despegar.vr.commons.maybe.Maybe
import com.despegar.vr.commons.workers.Workers
import SearchRequest
import SearchRequest
import scaldi.{Injectable, Injector}

class ExampleService(implicit inj: Injector) extends Injectable {

  val routerServiceFactory = inject[RouterServiceFactory]
  val workers = inject[Workers]

  var routerService = routerServiceFactory.createRouterService()

  def search(searchRequest: SearchRequest)(implicit ctx: Context): Maybe[Array[Byte]] = {
    routerService.search(searchRequest)
  }


  def rebuild()(implicit ctx: Context): Unit = {
    val oldRouter = routerService
    routerService = routerServiceFactory.createRouterService()
    workers.schedule{ _ => oldRouter.stop }
  }

}
