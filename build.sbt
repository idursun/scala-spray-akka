name := "scala-akka1"

version := "1.0"

scalaVersion := "2.10.4"

resolvers ++= Seq(
    "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Akka snapshots" at "http://repo.akka.io/snapshots"
)

libraryDependencies ++= {
    val akkaV = "2.4-SNAPSHOT"
    Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaV withSources(),
        "com.typesafe.akka" %% "akka-persistence-experimental" % akkaV withSources(),
        "io.spray" %% "spray-json" % "1.3.1" withSources(),
        "io.spray" %% "spray-can" % "1.3.2" withSources(),
        "io.spray" %% "spray-routing" % "1.3.2" withSources(),
        "io.spray" %% "spray-testkit" % "1.3.2" % "test",
        //"org.specs2" %% "specs2" % "2.3.11" % "test"
        "org.scalatest" %% "scalatest" % "2.2.1" % "test"
    )
}