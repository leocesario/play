package com.despegar.indec.hangar.controllers

import com.despegar.indec.hangar.actions.RouterRequest
import com.despegar.indec.hangar.external.newrelic.service.NewRelicService
import com.despegar.indec.hangar.services.ExampleService
import com.despegar.vr.commons.cache.maybe.CacheHit
import com.despegar.vr.commons.controllers.{Controller, ResultHandler}
import RouterRequest
import RouterRequest
import scaldi.{Injectable, Injector}

class ExampleController(implicit val inj: Injector) extends Controller with Injectable with ResultHandler {

  val newRelicService = inject[NewRelicService]
  val searchService = inject[ExampleService]

  def hotCities() = Action[RouterRequest]()() { implicit request =>
    newRelicService.getHotCities(request){
      case CacheHit(cities) => Ok(cities) as JSON
    }
  }

  def totalAds() = Action[RouterRequest]()() { implicit request =>
    newRelicService.getTotalAdsFromCities(request){
      case CacheHit(totalAds) => Ok(totalAds) as JSON
    }
  }

  def rebuild() = Action[RouterRequest]()() { implicit request =>
    searchService.rebuild()
    Ok()
  }


}
