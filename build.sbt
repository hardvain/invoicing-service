name := "vanderbron-assignment"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.2"
  val mongoDriverVersion = "1.2.0-beta1"
  Seq(
    "io.spray"            %%  "spray-can"         % sprayVersion,
    "io.spray"            %%  "spray-routing"     % sprayVersion,
    "io.spray"            %% "spray-json"         % sprayVersion,
    "com.typesafe.akka"   %%  "akka-actor"        % akkaVersion,
    "org.mongodb.scala"   %% "mongo-scala-driver" % mongoDriverVersion,
    "io.spray"            %%  "spray-testkit"     % sprayVersion  % "test",
    "com.typesafe.akka"   %%  "akka-testkit"      % akkaVersion   % "test"
  )
}