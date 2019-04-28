name := "hatena-bookmark-filter"
version := "2.0.4"

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
      dependencies.akkaActor,
      dependencies.akkaStream,
      dependencies.akkaSlf4j,
      dependencies.slackScalaClient,
      dependencies.scalaScraper,
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
    val logback          = "ch.qos.logback"             % "logback-classic"     % "1.2.3"
    val scalaLogging     = "com.typesafe.scala-logging" %% "scala-logging"      % "3.9.2"
    val scalaCompiler    = "org.scala-lang"             % "scala-compiler"      % "2.12.8"
    val scalaXml         = "org.scala-lang.modules"     %% "scala-xml"          % "1.2.0"
    val slick            = "com.typesafe.slick"         %% "slick"              % "3.3.0"
    val slickHikaricp    = "com.typesafe.slick"         %% "slick-hikaricp"     % "3.3.0"
    val slickCodegen     = "com.typesafe.slick"         %% "slick-codegen"      % "3.3.0"
    val h2               = "com.h2database"             % "h2"                  % "1.4.197"
    val sttpCore         = "com.softwaremill.sttp"      %% "core"               % "1.5.14"
    val scalatest        = "org.scalatest"              %% "scalatest"          % "3.0.7"
    val scalactic        = "org.scalactic"              %% "scalactic"          % "3.0.7"
    val flywayCore       = "org.flywaydb"               % "flyway-core"         % "5.2.4"
    val akkaActor        = "com.typesafe.akka"          %% "akka-actor"         % "2.5.22"
    val akkaStream       = "com.typesafe.akka"          %% "akka-stream"        % "2.5.22"
    val akkaSlf4j        = "com.typesafe.akka"          %% "akka-slf4j"         % "2.5.22"
    val slackScalaClient = "com.github.gilbertw1"       %% "slack-scala-client" % "0.2.3"
    val scalaScraper     = "net.ruippeixotog"           %% "scala-scraper"      % "2.1.0"
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.scalaLogging
)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
  assemblyJarName in assembly := name.value + ".jar"
)
