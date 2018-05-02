package akka.stream.checkpoint.scaladsl

import akka.NotUsed
import akka.stream.checkpoint.{CheckpointRepositoryFactory, CheckpointStage}
import akka.stream.scaladsl.{Flow, Source}

object Checkpoint {

  def apply[T](name: String)(implicit factory: CheckpointRepositoryFactory): Flow[T, T, NotUsed] =
    Flow.fromGraph(CheckpointStage[T](repository = factory.createRepository(name)))
}

object Implicits {

  final implicit class FlowMetricsOps[In, Out, Mat](val flow: Flow[In, Out, Mat]) extends AnyVal {

    def checkpoint(name: String)(implicit factory: CheckpointRepositoryFactory): Flow[In, Out, Mat] =
      flow.via(CheckpointStage(repository = factory.createRepository(name)))
  }

  final implicit class SourceMetricsOps[Out, Mat](val source: Source[Out, Mat]) extends AnyVal {

    def checkpoint(name: String)(implicit factory: CheckpointRepositoryFactory): Source[Out, Mat] =
      source.via(CheckpointStage(repository = factory.createRepository(name)))
  }
}