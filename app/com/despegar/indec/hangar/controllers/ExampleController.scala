package com.despegar.indec.hangar.controllers

import com.despegar.indec.hangar.actions.HangarRequest
import com.despegar.indec.hangar.api.ExampleRequest
import com.despegar.indec.hangar.services.ExampleService
import com.despegar.vr.commons.controllers.{Controller, ResultHandler}
import scaldi.Injector

class ExampleController(implicit val inj: Injector) extends Controller with ResultHandler {

  val exampleService = inject[ExampleService]

  def exampleRoute() = Action[HangarRequest]()() { implicit request =>
    Ok(exampleService.exampleMethod)
  }

  def anotherExampleRoute(id: String) =  Action[HangarRequest,ExampleRequest]()() { implicit request =>
    Ok(request.content)
  }

}
