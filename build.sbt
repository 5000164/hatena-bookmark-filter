name := "hatena-bookmark-filter"
version := "1.0.0"
scalaVersion := "2.12.6"
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.2.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3"
