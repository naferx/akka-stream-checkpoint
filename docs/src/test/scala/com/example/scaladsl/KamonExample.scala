package com.example.scaladsl

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.checkpoint.scaladsl.Checkpoint
import akka.stream.scaladsl.Source

object KamonExample extends App {
  implicit val system       = ActorSystem.create("KamonScalaExample")
  implicit val materializer = ActorMaterializer.create(system)
  import system.dispatcher

  // #kamon
  import akka.stream.checkpoint.KamonBackend._

  Source(1 to 100)
    .via(Checkpoint("produced"))
    .filter((x: Int) ⇒ x % 2 == 0)
    .via(Checkpoint("filtered"))
    .runForeach(println)
  // #kamon
    .onComplete { _ ⇒  system.terminate() }
}
