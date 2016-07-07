package com.despegar.indec.hangar.utils.serialization

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods
import org.json4s.{Extraction, Serializer}

object Json4sSnakeCaseSerializer extends Json4sSnakeCaseSerializer(Nil)

class Json4sSnakeCaseSerializer(customSerializers: List[Serializer[_]]) extends Json4sSerializer(customSerializers) {

  override def write(obj: Any): String = {
    JsonMethods.mapper.writeValueAsString(Extraction.decompose(obj).safeSnakizeKeys)
  }

  def parse(json: String): JValue = {
    JsonMethods.parse(json).safeSnakizeKeys
  }

}