
val commonSettings = Seq(
  organization := "com.github.svezfaz",
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
    "-Xfatal-warnings",
    "-Ywarn-dead-code",
    "-Ywarn-unused-import",
    "-Ywarn-unused",
    "-Ywarn-nullary-unit"
  )
)

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val root = project.in(file("."))
  .settings(commonSettings, noPublish)
  .aggregate(core, dropwizard, kamon, benchmarks, docs)

lazy val core = checkpointProject("core", Dependencies.core)

lazy val dropwizard = checkpointProject("dropwizard", Dependencies.dropwizard)
  .dependsOn(core)

lazy val kamon = checkpointProject("kamon", Dependencies.kamon)
  .dependsOn(core)

lazy val benchmarks = checkpointProject("benchmarks", noPublish)
  .enablePlugins(JmhPlugin)
  .dependsOn(dropwizard)

lazy val docs =
  checkpointProject(
    "docs",
    noPublish,
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    paradoxNavigationDepth := 3,
    paradoxProperties ++= Map(
      "version"                   → version.value,
      "akkaVersion"               → Dependencies.akkaVersion,
      "scalaVersion"              → scalaVersion.value,
      "scalaBinaryVersion"        → scalaBinaryVersion.value,
      "dropwizardVersion"         → Dependencies.dropwizardVersion,
      "kamonVersion"              → Dependencies.kamonVersion,
      "extref.akka-docs.base_url" → s"http://doc.akka.io/docs/akka/${Dependencies.akkaVersion}/%s",
      "extref.dw-docs.base_url"   → s"http://metrics.dropwizard.io/${Dependencies.dropwizardVersion}/getting-started",
      "extref.kamon-docs.base_url" → "http://kamon.io/documentation/1.x/get-started"
    ),
    Dependencies.docs
  )
  .enablePlugins(ParadoxPlugin)
  .dependsOn(dropwizard, kamon)

def checkpointProject(projectId: String, additionalSettings: sbt.Def.SettingsDefinition*): Project =
  Project(id = projectId, base = file(projectId))
    .settings(commonSettings)
    .settings(name := s"akka-stream-checkpoint-$projectId")
    .settings(additionalSettings: _*)