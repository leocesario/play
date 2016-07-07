package com.despegar.indec.hangar.utils.serialization

import java.util.Locale._

import com.despegar.indec.hangar.utils.serialization.custom.{Json4sDateTimeSerializer, Json4sLocalDateSerializer, Json4sStringListsSerializer, Json4sStringSetsSerializer}
import org.json4s.JsonAST.{JField, JValue}
import org.json4s.jackson.{JsonMethods, Serialization}
import org.json4s.{Formats, MonadicJValue, NoTypeHints, Serializer => J4SONSerializer, _}

abstract class Json4sSerializer(customSerializers: List[J4SONSerializer[_]] = Nil) extends JsonSerializer {

  implicit def formats: Formats =
    Serialization.formats(NoTypeHints) +
      Json4sLocalDateSerializer +
      Json4sDateTimeSerializer +
      Json4sStringListsSerializer +
      Json4sStringSetsSerializer ++ customSerializers

  override def read[T](json: String)(implicit mf: Manifest[T]): T = {
    val ast = JsonMethods.parse(json, formats.wantsBigDecimal)
    ast.safeCamelizeKeys.extract(formats = formats, mf)
  }

  def extract[T](jValue: JValue)(implicit mf: Manifest[T]): T = {
    jValue.safeCamelizeKeys.extract(formats = formats, mf)
  }

  implicit class SafeKeyTransformingMonadicJValue(jv: JValue) extends MonadicJValue(jv) {

    def safeCamelizeKeys = rewriteJsonAST(camelize = true)

    def safeSnakizeKeys = rewriteJsonAST(camelize = false)

    // Method that needs to be overriden to prevent snakizing and camilizing map keys...
    private[this] def rewriteJsonAST(camelize: Boolean): JValue =
      transformField {
        case JField(nm, x) if !nm.startsWith("_") && !isMapKey(nm) => JField(if (camelize) this.camelize(nm) else underscore(nm), x)
      }

    private def isMapKey(key: String): Boolean = key.replaceAll("_", "").replaceAll("\\d", "").matches("^[A-Z]+$")

    // Json4s methods MonadicJValue private methods
    private[this] def camelize(word: String): String = {
      val w = pascalize(word)
      w.substring(0, 1).toLowerCase(ENGLISH) + w.substring(1)
    }

    private[this] def pascalize(word: String): String = {
      val lst = word.split("_").toList
      (lst.headOption.map(s ⇒ s.substring(0, 1).toUpperCase(ENGLISH) + s.substring(1)).get ::
        lst.tail.map(s ⇒ s.substring(0, 1).toUpperCase + s.substring(1))).mkString("")
    }

    private[this] def underscore(word: String): String = {
      val spacesPattern = "[-\\s]".r
      val firstPattern = "([A-Z]+)([A-Z][a-z])".r
      val secondPattern = "([a-z\\d])([A-Z])".r
      val replacementPattern = "$1_$2"
      spacesPattern.replaceAllIn(secondPattern.replaceAllIn(firstPattern.replaceAllIn(word, replacementPattern), replacementPattern), "_").toLowerCase
    }
  }

}

