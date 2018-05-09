package com.example.scaladsl

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.checkpoint.{CheckpointBackend, CheckpointRepository}
import akka.stream.checkpoint.scaladsl.Checkpoint
import akka.stream.scaladsl.Source

object CustomBackendExample extends App {
  implicit val system       = ActorSystem.create("CustomBackendScalaExample")
  implicit val materializer = ActorMaterializer.create(system)
  import system.dispatcher

  // #custom
  implicit val loggingBackend: CheckpointBackend = new CheckpointBackend {
    override def createRepository(name: String): CheckpointRepository = new CheckpointRepository {

      override def markPush(latencyNanos: Long, backpressureRatio: Long): Unit =
        println(s"PUSH - $name: latency:$latencyNanos, backpressure ratio:$backpressureRatio")

      override def markPull(latencyNanos: Long): Unit =
        println(s"PULL - $name: latency:$latencyNanos")
    }
  }

  Source(1 to 100)
    .via(Checkpoint("produced"))
    .filter((x: Int) ⇒ x % 2 == 0)
    .via(Checkpoint("filtered"))
    .runForeach(println)
  // #custom
    .onComplete { _ ⇒  system.terminate() }
}
