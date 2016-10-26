name := "vanderbron-assignment"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayVersion,
    "io.spray"            %%  "spray-routing" % sprayVersion,
    "com.typesafe.akka"   %%  "akka-actor"    % akkaVersion,
    "io.spray"            %%  "spray-testkit" % sprayVersion  % "test",
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaVersion   % "test",
    "org.mongodb.scala" %% "mongo-scala-driver" % "1.2.0-beta1",
    "org.mongodb" % "mongodb-driver" % "3.2.2"
  )
}