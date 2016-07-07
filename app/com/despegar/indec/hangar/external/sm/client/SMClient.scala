package com.despegar.indec.hangar.external.sm.client

import com.despegar.indec.hangar.api.SearchRequest
import com.despegar.vr.commons.maybe.Maybe
import com.despegar.vr.commons.rest.{Rest, RestClient}
import SearchRequest
import SearchRequest

trait SMClient {
  this: RestClient  =>

  val SearchPath = "vrsm/ads"
  val dateFormat = "yyyy-MM-dd"
  def isActive = true

  def search(request: SearchRequest)(implicit rest: Rest): Maybe[Array[Byte]] = {
    performAsBytes(_.withQueryString(request:_*).get(), SearchPath, "SEARCH_ADS")
  }

  def checkAndUpdate()(implicit rest: Rest): Boolean = true

  implicit def requestToQueryString(request: SearchRequest): List[(String,String)] = {
    (Some("city_code" -> request.cityCode) ::
      request.checkIn.map( "check_in" -> _.toString(dateFormat)) ::
      request.checkOut.map( "check_out" -> _.toString(dateFormat)) ::
      Some( "currencies" -> request.currencies.mkString(",")) ::
      request.imagesQuantity.map("images_quantity" -> _.toString) ::
      request.page.map("page" -> _.toString) ::
      request.pageSize.map("page_size" -> _.toString) ::
      request.priceRange.map(p => "price_range" -> s"${p.min},${p.max},${p.currency}") ::
      request.amenities.map( "amenities" -> _.values.mkString(",")) ::
      request.neighbourhoods.map( "neighbourhoods" -> _.values.mkString(",")) ::
      request.propertyTypes.map( "property_types" -> _.values.mkString(",")) ::
      request.status.map( "status" -> _.values.mkString(",")) ::
      request.bedsQuantities.map( "beds_quantities" -> _.values.mkString(",")) ::
      request.sortCriteria.map( "sort_criteria" -> _) ::
      request.guestsQuantity.map( "guests_quantity" -> _.toString) ::
      request.withFacets.map( "with_facets" -> _.toString) ::
      Nil).flatten
  }


}
