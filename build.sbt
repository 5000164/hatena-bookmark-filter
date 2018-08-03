name := "hatena-bookmark-filter"
version := "1.1.1"

lazy val common = project
  .settings(
    name := "common",
    commonSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.scalaCompiler,
      dependencies.slick,
      dependencies.slickHikaricp,
      dependencies.h2
    )
  )

lazy val collect = project
  .settings(
    name := "collect",
    commonSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.scalaXml,
      dependencies.sttpCore,
      dependencies.scalatest % "test",
      dependencies.scalactic % "test"
    )
  )
  .dependsOn(
    common
  )

lazy val post = project
  .settings(
    name := "post",
    commonSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sttpCore,
      dependencies.slackScalaClient,
      dependencies.scalatest % "test",
      dependencies.scalactic % "test"
    )
  )
  .dependsOn(
    common
  )

lazy val migration = project
  .settings(
    name := "migration",
    commonSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.flywayCore,
      dependencies.slick,
      dependencies.slickCodegen,
      dependencies.h2
    )
  )

lazy val dependencies =
  new {
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
    val scalaCompiler = "org.scala-lang" % "scala-compiler" % "2.12.6"
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
    val slick = "com.typesafe.slick" %% "slick" % "3.2.3"
    val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3"
    val slickCodegen = "com.typesafe.slick" %% "slick-codegen" % "3.2.3"
    val h2 = "com.h2database" % "h2" % "1.4.197"
    val sttpCore = "com.softwaremill.sttp" %% "core" % "1.2.1"
    val scalatest = "org.scalatest" %% "scalatest" % "3.0.5"
    val scalactic = "org.scalactic" %% "scalactic" % "3.0.5"
    val flywayCore = "org.flywaydb" % "flyway-core" % "5.1.4"
    val slackScalaClient = "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3"
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.scalaLogging
)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
  assemblyJarName in assembly := name.value + ".jar"
)
