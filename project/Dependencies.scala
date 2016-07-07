import sbt._

object Dependencies {


  /*
    val json4sVersion = "3.2.11"
  val json4sNative = "org.json4s" %% "json4s-native" % json4sVersion
  val json4sJackson = "org.json4s" %% "json4s-jackson" % json4sVersion
  val json4sExt = "org.json4s" %% "json4s-ext" % json4sVersion
  val json4s = Seq(json4sNative, json4sJackson, json4sExt)

  val jacksonVersion = "2.7.3"
  val jacksonModule = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
  val jacksonCore = "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.4-VR"
  val jackson = Seq(jacksonCore, jacksonModule)

  val rest = Seq("com.ning" % "async-http-client" % "1.9.31")

  val playVersion = "2.3.8"
  val playCore = "com.typesafe.play" %% "play" % playVersion
  val playCache = "com.typesafe.play" %% "play-cache" % playVersion
  val playWs = "com.typesafe.play" %% "play-ws" % playVersion
  val play = Seq(playCore, playCache)

  val scaldiVersion = "0.5.3"
  val scaldiCore = "org.scaldi" %% "scaldi" % scaldiVersion
  val scaldiPlay = "org.scaldi" %% "scaldi-play" % scaldiVersion
  val scaldi = Seq(scaldiCore, scaldiPlay)

  val google = Seq("com.google.guava" % "guava" % "18.0")

  val appender = "com.despegar.library" % "logging" % "0.0.3"
  var newrelicAgent = "com.newrelic.agent.java" % "newrelic-agent" % "3.25.0"
  var newrelicApi = "com.newrelic.agent.java" % "newrelic-api" % "3.25.0"
  val logging = Seq(newrelicAgent, newrelicApi, appender)

  val nScalaTime = "com.github.nscala-time" %% "nscala-time" % "1.8.0"
  val time = Seq(nScalaTime).map(_.withSources())
   */
  val vrCommonsCore = "com.despegar.vr" %% "commons-core" % "3.0.1"

  val vrCommonsMetadata = "com.despegar.vr" %% "commons-metadata" % "2.0.0"

  val dependencies = Seq(vrCommonsCore, vrCommonsMetadata)

}
