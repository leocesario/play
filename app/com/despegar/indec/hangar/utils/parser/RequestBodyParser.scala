package com.despegar.indec.hangar.utils.parser

import java.net.URLDecoder

import com.despegar.indec.hangar.utils.logging.Logging
import com.despegar.vr.commons.concurrent.ExecutionContext.defaultContext
import com.despegar.vr.commons.serialization.json.{Json4sSnakeCaseSerializer, JsonSerializer}
import com.despegar.vr.commons.utils.ExceptionUtils
import com.fasterxml.jackson.databind.node.{ArrayNode, ObjectNode}
import org.json4s.JsonAST.JNothing
import org.json4s.jackson.JsonMethods
import play.api.libs.iteratee.Iteratee
import play.api.mvc.Results.BadRequest
import play.api.mvc.{BodyParser => PlayBodyParser, RequestHeader => PlayRequestHeader, Result => PlayResult}
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success, Try}

case class RequestBodyParser[T: Manifest](implicit inj: Injector) extends PlayBodyParser[T] with Logging with Injectable {

  val serializer = inject[JsonSerializer](identified by 'BodyParserSerializer is by default Json4sSnakeCaseSerializer)

  def apply(playRequest: PlayRequestHeader): Iteratee[Array[Byte], Either[PlayResult, T]] = {

    val iteratee: Iteratee[Array[Byte], Array[Byte]] = Iteratee.consume[Array[Byte]]()

    iteratee.map[Either[PlayResult, T]] { bytes =>
      RequestResolver.resolveRequest[T](playRequest, bytes, serializer) match {
        case Success(obj) => Right(obj)
        case Failure(ex) => Left(BadRequest(serializer.write(Map("message" -> ex.getMessage, "stackTrace" -> ExceptionUtils.getStackTrace(ex)))))
      }
    }

  }

}

object RequestResolver extends Logging {

  private val FIELD = "([^\\.]+)(?:\\.(.*))?".r
  private val ARRAY = "([^\\.]+)\\[([0-9]+)\\](?:\\.(.*))?".r
  private val INT = "^(-?\\d{1,9})$".r
  private val LONG = "^(-?\\d{10,18})$".r
  private val DOUBLE = "^(-?\\d+\\.\\d+)$".r
  private val BOOLEAN = "^(true|false|TRUE|FALSE)$".r
  private val NULL = "^(null)$".r
  private val ENCODING = "UTF-8"

  private val KEY_VALUE = "([^=]+)=?(.*)".r

  def resolveRequest[T: Manifest](request: PlayRequestHeader, bytes: Array[Byte], serializer: JsonSerializer): Try[T] = {

    val Body(bodyString) = bytes

    val bodyAST = request.contentType match {
      case Some("application/x-www-form-urlencoded") =>
        val values = for (pair <- bodyString.split("&")) yield Option(pair).collect { case KEY_VALUE(key, value) => savePrimitives(decode(key), decode(value))}
        val node = JsonMethods.mapper.createObjectNode()
        values.foreach( entry => entry.map(e => populate(node, e._1, e._2)) )
        serializer.parse(node.toString)
      case Some("application/json") => serializer.parse(bodyString)
      case _ => JNothing
    }

    val queryStringAST = serializer.parse(serializer.write(request.queryString.mapValues(_.head).map(savePrimitives)))
    val pathVariablesAST = serializer.parse(serializer.write(getPathVariables(request).map(savePrimitives)))

    Try(serializer.extract[T](pathVariablesAST merge bodyAST merge queryStringAST))

  }

  private def decode(bodyString: String): String =  URLDecoder.decode(bodyString, ENCODING)

  def getPathVariables(request: PlayRequestHeader): Map[String, String] = {

    def extract(path:String, pathWithNames:String, vars: List[(String, String)]): List[(String, String)] = {

      val VARIABLE_NAME = "([^$]+)\\$([^<]*)[^>]+>(.*)".r
      val VARIABLE_VALUE = "/?([^/]+)(.*)".r

      pathWithNames match {
        case VARIABLE_NAME(prefix,name,names_suffix) =>
          val VARIABLE_VALUE(value, values_suffix) = path.substring(prefix.length)
          (name, value) :: extract(values_suffix, names_suffix, vars)
        case _ => vars
      }

    }

    request.tags.get("ROUTE_PATTERN").map(extract(request.path, _, Nil)).getOrElse(Nil).toMap

  }

  private def savePrimitives(tuple: (String, String)): (String, Any) = {
    tuple match {
      case (key, INT(value)) => (key, value.toInt)
      case (key, LONG(value)) => (key, value.toLong)
      case (key, BOOLEAN(value)) => (key, value.toBoolean)
      case (key, DOUBLE(value)) => (key, value.toDouble)
      case (key, NULL(value)) => (key, null)
      case (key, value) => (key, value)
    }
  }

  private def populate(parent: ObjectNode, path: String, value: Any): Unit = path match {
    case ARRAY(head, index, null) => parent.withArray(head).add(value.toString)
    case ARRAY(head, index, tail) => populate(parent.withArray(head).ensure(index.toInt), tail, value)
    case FIELD(head, null) => parent.put(head, value.toString)
    case FIELD(head, tail) => populate(parent.`with`(head), tail, value)
    case _ => throw new RuntimeException(s"Invalid request format: $path isn't supported")
  }

  implicit class RichJsonArray(value: ArrayNode) {
    def ensure(index: Int): ObjectNode = {
      value.size() to index foreach (x => value.addObject())
      value.get(index).asInstanceOf[ObjectNode]
    }
  }

  object Body {
    def unapply(body: Array[Byte]): Option[String] = {
      if (body.isEmpty) Some("{}")
      else Some(new String(body))
    }
  }

}
