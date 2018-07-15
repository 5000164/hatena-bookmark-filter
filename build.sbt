name := "hatena-bookmark-filter"
version := "1.0.0"
scalaVersion := "2.12.6"
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
libraryDependencies += "org.flywaydb" % "flyway-core" % "5.1.4"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.3"
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.3"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.23.1"
libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.2.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3"

mainClass in assembly := Some("interfaces.Application")
