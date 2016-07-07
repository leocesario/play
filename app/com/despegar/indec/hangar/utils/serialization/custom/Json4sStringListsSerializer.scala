package com.despegar.indec.hangar.utils.serialization.custom

import org.json4s.JsonAST.JString
import org.json4s._

case object Json4sStringListsSerializer extends CustomSerializer[List[_]](format => (
  {
    /**
     * Unused since deserialize is overriden
     */
    case JString(s) => Nil
    case JNull => Nil
    case JNothing => Nil
  },
  {
    /**
     * Serialize as normal Json list...
     */
    case list: List[_] => JArray(list.map(Extraction.decompose(_)(format)))
  }
  )
) {

  /**
   * Deserialize from comma separated values to list...
   */
  val deserializationFunction: PartialFunction[JValue, List[_]] = {

    case JString(s) => s.split(",").toList
    case JNull => Nil
    case JNothing => Nil
  }

  override def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), List[_]] = {
    case (TypeInfo(Class, _), json) if deserializationFunction.isDefinedAt(json) => deserializationFunction(json)
  }
}