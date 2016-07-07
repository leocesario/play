package com.despegar.indec.hangar.utils.serialization.custom

import org.json4s.JsonAST.JString
import org.json4s._

case object Json4sStringSetsSerializer extends CustomSerializer[Set[_]](format => (
  {
    /**
     * Unused since deserialize is overriden
     */
    case JString(s) => Set.empty
    case JNull => Set.empty
    case JNothing => Set.empty
  },
  {
    /**
     * Serialize as normal Json list...
     */
    case set: Set[_] => JArray(set.map(Extraction.decompose(_)(format)).toList)
  }
  )
) {

  /**
   * Deserialize from comma separated values to set...
   */
  val deserializationFunction: PartialFunction[JValue, Set[_]] = {

    case JString(s) => s.split(",").toSet
    case JNull => Set.empty
    case JNothing => Set.empty
  }

  override def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Set[_]] = {
    case (TypeInfo(Class, _), json) if deserializationFunction.isDefinedAt(json) => deserializationFunction(json)
  }
}