package com.despegar.indec.hangar.utils.serialization.custom

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json4s.JsonAST.JString
import org.json4s._

import scala.util.Try

case object Json4sDateTimeSerializer extends CustomSerializer[DateTime](format => ( {
  case JString(s) => Try(ISODateTimeFormat.dateTimeParser().parseDateTime(s)).getOrElse(null)
  case JNull => null
  case JNothing => null
}, {
  case d: DateTime => JString(Formatter.print(d.withZone(GMT)))
}
  ))
