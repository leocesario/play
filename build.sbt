lazy val root = (project in file(".")).enablePlugins(PlayScala)

organization := "com.despegar.vr"
name := "sm-router"
scalaVersion := "2.11.7"

resolvers += "miami" at "http://nexus:8080/nexus/content/groups/public/"
resolvers += "bsas" at "http://nexus.despegar.it:8080/nexus/content/groups/public/"

libraryDependencies ++= Dependencies.dependencies

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

filterScalaLibrary := true
cloudiaSettings
CloudiaKeys.main := "play.core.server.ProdServerStart"
CloudiaKeys.healthCheckUrl := "/vacation-rentals/metadata/sm-router"
CloudiaKeys.excludeLibs := Seq("commons-metadata_2.11-2.0.0.jar")

sourceGenerators in Compile <+= (sourceManaged in Compile, organization, name, version) map { (d, o, n, v) =>
  val file = d / "com" / "despegar" / "vr" / "BuildInfo.scala"
  IO.write(file, """package com.despegar.vr
                   |object BuildInfo {
                   |  val organization = "%s"
                   |  val name = "%s"
                   |  val version = "%s"
                   |}
                   | """.stripMargin.format(o, n, v))
  Seq(file)
}

sourceGenerators in Compile <+= (sourceManaged in Compile, libraryDependencies) map { (sm, dependencies) =>
  val file = sm / "com" / "despegar" / "vr" / "Commons.scala"

  val commons = dependencies.collect {
    case d if d.organization == "com.despegar.vr" && d.name.startsWith("commons") => s""" "${d.name.drop(8)}" -> "${d.revision}" """
  }

  IO.write(file, """package com.despegar.vr
                   |object Commons {
                   |  val dependencies:Map[String,String] = Map(%s)
                   |}
                   | """.stripMargin.format(commons.mkString(",")))
  Seq(file)
}