
val commonSettings = Seq(
  organization := "eu.svez",
  description := "Checkpoint stage to monitor Akka Streams streaming applications",
  crossScalaVersions := Seq("2.12.6", "2.11.12"),
  scalaVersion := crossScalaVersions.value.head,
  scalacOptions in Compile ++= Seq(
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xlint",
    "-Xfuture",
    "-Ywarn-dead-code",
    "-Ywarn-unused-import",
    "-Ywarn-unused",
    "-Ywarn-nullary-unit"
  )
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .settings(publishArtifact := false)
  .aggregate(benchmarks, core, dropwizard, examples)

lazy val benchmarks = checkpointProject("benchmarks").enablePlugins(JmhPlugin).dependsOn(dropwizard)

lazy val core = checkpointProject("core", Dependencies.core)

lazy val dropwizard = checkpointProject("dropwizard", Dependencies.dropwizard).dependsOn(core)

lazy val examples = checkpointProject("examples", Dependencies.examples ++ Seq(
    publishArtifact := false,
    scalacOptions in Compile ~= { _ filterNot { o ⇒ o == "-Xfatal-warnings" } }
  ))
  .dependsOn(dropwizard)

def checkpointProject(projectId: String, additionalSettings: sbt.Def.SettingsDefinition*): Project =
  Project(id = projectId, base = file(projectId))
    .settings(commonSettings)
    .settings(name := s"akka-stream-checkpoint-$projectId")
    .settings(additionalSettings: _*)