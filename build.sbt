name := "hatena-bookmark-filter"
version := "1.1.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
  assemblyJarName in assembly := name.value + ".jar",
  mainClass in assembly := Some("interfaces.Application")
)

lazy val collector = project
  .settings(
    name := "collector",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
      "org.flywaydb" % "flyway-core" % "5.1.4",
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "com.typesafe.slick" %% "slick-codegen" % "3.2.3",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
      "org.xerial" % "sqlite-jdbc" % "3.23.1",
      "com.softwaremill.sttp" %% "core" % "1.2.1",
      "org.scalactic" %% "scalactic" % "3.0.5",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3"
    )
  )
