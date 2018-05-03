package com.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.codahale.metrics.MetricRegistry

object CheckpointScalaPimpedExample extends App {
  implicit val system       = ActorSystem.create("stream-checkpoint-java-demo")
  implicit val materializer = ActorMaterializer.create(system)

  implicit val metricRegistry = new MetricRegistry()

  import akka.stream.checkpoint.DropwizardBackend._
  import akka.stream.checkpoint.scaladsl.Implicits._

  Source(1 to 100)
    .checkpoint("produced")
    .filter((x: Int) ⇒ x % 2 == 0)
    .checkpoint("filtered")
    .runForeach(println)
}
