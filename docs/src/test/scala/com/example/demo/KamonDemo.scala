package com.example.demo

import akka.stream.checkpoint.{CheckpointBackend, KamonBackend}

object KamonDemo extends CheckpointDemo {

  implicit val backend: CheckpointBackend = KamonBackend.instance

  kamon.Kamon.addReporter(new kamon.statsd.StatsDReporter())

  backpressuringAB
}