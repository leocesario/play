package com.despegar.indec.hangar.utils.logging

import com.despegar.vr.commons.logging.NewRelic

trait Logging {

  lazy val LOGGER = play.api.Logger(this.getClass)

  def trace(message: => String): Unit = LOGGER.trace(message)

  def debug(message: => String): Unit = LOGGER.debug(message)

  def info(message: => String): Unit = LOGGER.info(message)

  def warn(message: => String): Unit = LOGGER.warn(message)

  def error(message: => String): Unit = LOGGER.error(message)

  def error(message: => String, exception: Throwable): Unit = {
    LOGGER.error(message, exception)
    NewRelic.noticeError(exception, Map("msg" -> message))
  }

}