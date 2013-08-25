name := "ScalaBot"
     
version := "1.0"
     
scalaVersion := "2.10.2"
     
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
     
libraryDependencies +=
    "com.typesafe.akka" %% "akka-actor" % "2.2.0"

mainClass in (Compile, run) := Some("tpw_rules.irc.scalabot.ScalaBot")
