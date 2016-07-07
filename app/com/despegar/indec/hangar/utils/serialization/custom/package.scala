package com.despegar.indec.hangar.utils.serialization

import java.util.TimeZone

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import org.joda.time.DateTimeZone
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatter}
import org.json4s.jackson.JsonMethods

package object custom {
  val Formatter: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis
  val GMT: DateTimeZone = DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT"))

  JsonMethods.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  JsonMethods.mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)

  JsonMethods.mapper.getVisibilityChecker.withFieldVisibility(Visibility.PUBLIC_ONLY)
  JsonMethods.mapper.getVisibilityChecker.withCreatorVisibility(Visibility.NONE)
  JsonMethods.mapper.getVisibilityChecker.withGetterVisibility(Visibility.NONE)
  JsonMethods.mapper.getVisibilityChecker.withSetterVisibility(Visibility.NONE)
}
