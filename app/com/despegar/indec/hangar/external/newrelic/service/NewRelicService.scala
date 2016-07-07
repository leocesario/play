package com.despegar.indec.hangar.external.newrelic.service

import com.despegar.indec.hangar.actions.Context
import com.despegar.indec.hangar.external.newrelic.model.TotalAds
import com.despegar.vr.commons.cache.Cache
import com.despegar.vr.commons.maybe.{Maybe, Something, UnexpectedResult}
import com.despegar.vr.commons.newrelic.client.NewRelicInsightClient
import com.despegar.vr.commons.newrelic.model.NewRelicResult
import com.despegar.vr.commons.rest.Ok
import scaldi.{Injectable, Injector}

class NewRelicService(implicit inj: Injector) extends Injectable {

  val client = inject[NewRelicInsightClient]
  val citiesLimit = inject[Int]("newrelic.cities")
  val cache = inject[Cache]
  val SECONDS_TTL = 86400
  val HOT_CITIES = "HOT_CITIES"
  val TOTAL_ADS = "TOTAL_ADS"

  val hotCitiesQuery = s"SELECT count(*) from Search WHERE AppsChain LIKE '%ui-search%' FACET city SINCE 1 day ago LIMIT ${citiesLimit.toString}"
  val totalAdsQuery = s"SELECT max(totalAds) FROM Search FACET city WHERE city IN {CITIES} SINCE 1 day ago LIMIT ${(citiesLimit+10).toString}"

  def getHotCities(implicit ctx: Context): Maybe[List[String]] = {
    cache.getAsMaybe[List[String]](HOT_CITIES,SECONDS_TTL)(hotCities)
  }

  def getTotalAdsFromCities(implicit ctx: Context): Maybe[List[TotalAds]] = {
    cache.getAsMaybe[List[TotalAds]](TOTAL_ADS,SECONDS_TTL)(totalAds)
  }

  def hotCities(implicit ctx: Context): Maybe[List[String]] = {
    executeQuery(hotCitiesQuery).map(_.facets.map(_.name))
  }

  def totalAds(implicit ctx: Context): Maybe[List[TotalAds]] = {
    for {
      hotCities <- getHotCities
      citiesQuery = hotCities.mkString("('","', '", "')")

      newRelicResult <- executeQuery(totalAdsQuery.replace("{CITIES}",citiesQuery))

      totals = newRelicResult.facets.map( o => TotalAds(o.name,o.results.head.max.get.toInt))

      ordered = totals.sortBy( t => hotCities.indexOf(t.city)).dropWhile( t => !hotCities.contains(t.city))

    } yield ordered
  }

  private def executeQuery(nrql: String)(implicit ctx: Context): Maybe[NewRelicResult] = {
    client.executeQuery(nrql) match {
      case Ok(result) => Something(result)
      case _ => UnexpectedResult(s"Error executing new relic query: $nrql")
    }
  }

}
