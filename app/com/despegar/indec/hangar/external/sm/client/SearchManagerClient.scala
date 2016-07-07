package com.despegar.indec.hangar.external.sm.client

import com.despegar.indec.hangar.api.SearchRequest
import com.despegar.indec.hangar.external.sm.model.ClusterInfo
import com.despegar.vr.commons.logging.Logging
import com.despegar.vr.commons.maybe.Maybe
import com.despegar.vr.commons.rest.{Rest, RestClient, Serialization}
import com.despegar.vr.commons.serialization.Serializer
import com.despegar.vr.commons.serialization.json.faster.FasterXMLSnakeCaseSerializer
import scaldi.{Injectable, Injector}

class SearchManagerClient(implicit inj: Injector) extends Injectable with RestClient  with Serialization with SMClient with Logging {

  override val protocol: String = inject[String]("sm.protocol")
  override val host: String = inject[String]("sm.host")
  override val timeout: Int = inject[Int]("sm.timeout")
  override val serializer: Serializer = FasterXMLSnakeCaseSerializer

  val ClusterInfo = "vacation-rentals/metadata/search-manager/cluster-info"

  def clusterInfo()(implicit rest: Rest): Maybe[ClusterInfo] = {
    performAsObject[ClusterInfo](_.get(), ClusterInfo, "SEARCH_MANAGER_CLUSTER_INFO")
  }

  override def search(request: SearchRequest)(implicit rest: Rest): Maybe[Array[Byte]] = {
    val vrsmHeader = "X-Version-Override" -> "vr-sm-ads=vr-sm"
    performAsBytes(_.withQueryString(request:_*).withHeaders(vrsmHeader).get(), SearchPath, "SEARCH_ADS")
  }

}
