name := """Graphql-Forum"""

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala, DebianPlugin)

resolvers += Resolver.sonatypeRepo("snapshots")
maintainer in Linux := "Jeff Registre <jeffreyregistre@gmail.com>"

packageSummary in Linux := "GraphQL Forum test bench with Sangria and Play framework."

packageDescription := "GraphQL Api server built with Play framework and Sangria library."


scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies += "com.typesafe.play" %% "play-slick" %  "3.0.0-M5"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0-M5"
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"

libraryDependencies += "org.sangria-graphql" % "sangria_2.12" % "1.2.2"
libraryDependencies += "org.sangria-graphql" % "sangria-play-json_2.12" % "1.0.3"


libraryDependencies += specs2 % Test
  

