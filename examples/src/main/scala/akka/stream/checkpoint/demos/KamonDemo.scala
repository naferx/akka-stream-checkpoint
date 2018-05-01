package akka.stream.checkpoint.demos

import akka.stream.checkpoint.{CheckpointDemo, CheckpointRepositoryFactory, Kamon}

object KamonDemo extends CheckpointDemo {

  implicit val repositoryFactory: CheckpointRepositoryFactory = Kamon.factory

  kamon.Kamon.addReporter(new kamon.statsd.StatsDReporter())

  backpressuringAB
}