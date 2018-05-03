package akka.stream.checkpoint.demos

import akka.stream.checkpoint.{CheckpointBackend, CheckpointDemo, KamonBackend}

object KamonDemo extends CheckpointDemo {

  implicit val backend: CheckpointBackend = KamonBackend.instance

  kamon.Kamon.addReporter(new kamon.statsd.StatsDReporter())

  backpressuringAB
}