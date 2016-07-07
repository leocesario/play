package com.despegar.indec.hangar.utils.newrelic

import java.util

import com.despegar.vr.commons.utils.JavaConversions._
import com.newrelic.api.agent.{NewRelic => Agent}
import org.joda.time.{DateTime, LocalDate, LocalTime}

object NewRelic {

  def noticeError(message: String): Unit = {
    Agent.noticeError(message)
  }

  def noticeError(message: String, params: Map[String, String]): Unit = {
    Agent.noticeError(message, params)
  }

  def noticeError(throwable: Throwable): Unit = {
    Agent.noticeError(throwable)
  }

  def noticeError(throwable: Throwable, params: Map[String, String]): Unit = {
    Agent.noticeError(throwable, params)
  }

  def metric(event:String, attributes:(String, Any)*): Unit = {
    metric(event, attributes.toMap)
  }

  /*
  //TODO recibir si es robot.
  def metric(event:String, attributes:Map[String, Any])(implicit context:Headers): Unit = if(!context.headers.contains("XD-Automation")){
    Agent.getAgent.getInsights.recordCustomEvent(event, toNewRelicFormat(attributes))
  }
  */

  def toNewRelicFormat(attributes:Map[String, Any]): util.Map[String, AnyRef] = {
    attributes.map {
      case (key: String, value: Int) => (key, Int.box(value))
      case (key: String, value: Long) => (key, Long.box(value))
      case (key: String, value: Double) => (key, Double.box(value))
      case (key: String, value: Float) => (key, Float.box(value))
      case (key: String, value: Boolean) => (key, value.toString)
      case (key: String, value: BigDecimal) => (key, Double.box(value.toDouble))
      case (key: String, value: LocalDate) => (key, Long.box(value.toDateTime(LocalTime.MIDNIGHT).getMillis))
      case (key: String, value: DateTime) => (key, Long.box(value.getMillis))
      case (key: String, value: Any) => (key, value.toString)
    } // ++ commonsAttributes
  }


  /*
  private def commonsAttributes(implicit context:Headers): List[(String,String)] = {
    ("UOW" -> context.uow) ::
      ("AppsChain" -> context.appsChain) ::
      ("AppReferrer" -> context.appReferrer) ::
      ("RequestId" -> context.requestId) ::
      ("vrSession" -> context.session) ::
      ("Xservice" -> context.service) ::
      ("site" -> context.xSite) :: Nil
  }
  */


}