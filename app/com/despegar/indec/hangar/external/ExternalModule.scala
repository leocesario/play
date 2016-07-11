package com.despegar.indec.hangar.external

import com.despegar.vr.commons.newrelic.client.NewRelicInsightClient
import com.despegar.vr.commons.scaldiutils.ScaldiModule

class ExternalModule extends ScaldiModule {

  binding toNonLazy new NewRelicInsightClient

}
