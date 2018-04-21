
val akkaV       = "2.5.12"
val dropwizardV = "4.0.2"

lazy val root = (project in file("."))
  .settings(
    name := "akka-stream-monitoring-demo",
    version := "1.0",
    scalaVersion := "2.12.4",
    fork in run := true,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream"         % akkaV,
      "com.typesafe.akka" %% "akka-slf4j"          % akkaV,
      "org.slf4j"         %  "slf4j-api"           % "1.7.16"  % Runtime,
      "ch.qos.logback"    %  "logback-classic"     % "1.1.5"   % Runtime,
      "io.dropwizard.metrics" % "metrics-core"     % dropwizardV,
      "io.dropwizard.metrics" % "metrics-graphite" % dropwizardV,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    )
  )
