package com.despegar.indec.hangar.external.sm.client

import java.util.concurrent.atomic.AtomicBoolean

import akka.actor.{PoisonPill, ActorRef, ActorSystem, Props}
import com.despegar.indec.hangar.actors.ClientAliveActor
import com.despegar.indec.hangar.api.SearchRequest
import com.despegar.vr.commons.logging.Logging
import com.despegar.vr.commons.maybe.Maybe
import com.despegar.vr.commons.rest.{Rest, RestClient, Serialization}
import com.despegar.vr.commons.serialization.Serializer
import com.despegar.vr.commons.serialization.json.faster.FasterXMLSnakeCaseSerializer
import SearchRequest
import SearchRequest
import scaldi.{Injectable, Injector}

class SearchManagerCustomClient(val ipHost: String)(implicit inj: Injector) extends Injectable with RestClient with Serialization with SMClient with Logging {

   override val host: String = s"$ipHost:9290"
   override val protocol: String = inject[String]("sm.protocol")
   override val timeout: Int = inject[Int]("sm.timeout")
   override val serializer: Serializer = FasterXMLSnakeCaseSerializer
   val enabled: AtomicBoolean = new AtomicBoolean(true)
   val actorSystem = inject[ActorSystem]
   val actor: Option[ActorRef] = Option(actorSystem.actorOf(Props(new ClientAliveActor(this))))

   val Metadata = "vacation-rentals/metadata/search-manager"

   override def isActive = enabled.get()

   override def checkAndUpdate()(implicit rest: Rest): Boolean = {
     val alive = performAsUnit(_.get(), Metadata, "SEARCH_MANAGER_METADATA").isDefined
     if (alive!=enabled.get()) {
       enabled.set(alive)
       info(s"Client $ipHost changing to enabled=${alive.toString}")
     }

     alive
   }

  override def search(request: SearchRequest)(implicit rest: Rest): Maybe[Array[Byte]] = {
    info(s"Request send it to $host")
    super.search(request)
  }

  def disabled(): Unit = enabled.set(false)

  def stop(): Unit = { actor.foreach(_ ! PoisonPill) }

 }
