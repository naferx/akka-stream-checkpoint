package com.example.scaladsl

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.checkpoint.scaladsl.Checkpoint
import akka.stream.scaladsl.Source
import com.codahale.metrics.MetricRegistry

object DropwizardExample extends App {
  implicit val system       = ActorSystem.create("DropwizardScalaExample")
  implicit val materializer = ActorMaterializer.create(system)

  // #dropwizard
  implicit val metricRegistry = new MetricRegistry()

  import akka.stream.checkpoint.DropwizardBackend._

  Source(1 to 100)
    .via(Checkpoint("produced"))
    .filter((x: Int) ⇒ x % 2 == 0)
    .via(Checkpoint("filtered"))
    .runForeach(println)
  // #dropwizard
}
