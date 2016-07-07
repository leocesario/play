package com.despegar.indec.hangar.utils.serialization

import com.despegar.vr.commons.serialization.Serializer
import org.json4s.JsonAST.JValue

trait JsonSerializer extends Serializer {

  def parse(json: String): JValue

  def extract[T](jValue: JValue)(implicit mf: Manifest[T]): T

}