package com.despegar.indec.hangar.utils.serialization.custom

import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat
import org.json4s.JsonAST.JString
import org.json4s._

import scala.util.Try

case object Json4sLocalDateSerializer extends CustomSerializer[LocalDate](format => ( {
  case JString(s) => Try(ISODateTimeFormat.localDateParser().parseLocalDate(s)).getOrElse(null)
  case JNull => null
  case JNothing => null
}, {
  case d: LocalDate => JString(d.toString("yyyy-MM-dd"))
}
  )
                                                                         )
