package com.despegar.indec.hangar.external

import com.despegar.indec.hangar.external.newrelic.service.NewRelicService
import com.despegar.indec.hangar.external.sm.client.SearchManagerClient
import com.despegar.vr.commons.newrelic.client.NewRelicInsightClient
import com.despegar.vr.commons.scaldiutils.ScaldiModule

class ExternalModule extends ScaldiModule {

  binding toNonLazy new SearchManagerClient

  binding toNonLazy new NewRelicService
  binding toNonLazy new NewRelicInsightClient

}
