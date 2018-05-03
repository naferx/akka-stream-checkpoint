package com.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.checkpoint.scaladsl.Checkpoint
import akka.stream.scaladsl.Source
import com.codahale.metrics.MetricRegistry

object CheckpointScalaExample extends App {
  implicit val system       = ActorSystem.create("stream-checkpoint-java-demo")
  implicit val materializer = ActorMaterializer.create(system)

  implicit val metricRegistry = new MetricRegistry()

  import akka.stream.checkpoint.DropwizardBackend._

  Source(1 to 100)
    .via(Checkpoint("produced"))
    .filter((x: Int) ⇒ x % 2 == 0)
    .via(Checkpoint("filtered"))
    .runForeach(println)
}
