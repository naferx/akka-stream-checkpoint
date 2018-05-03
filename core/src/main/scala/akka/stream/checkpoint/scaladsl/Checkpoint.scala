package akka.stream.checkpoint.scaladsl

import akka.NotUsed
import akka.stream.checkpoint.{CheckpointBackend, CheckpointStage}
import akka.stream.scaladsl.{Flow, Source}

object Checkpoint {

  def apply[T](name: String)(implicit backend: CheckpointBackend): Flow[T, T, NotUsed] =
    Flow.fromGraph(CheckpointStage[T](repository = backend.createRepository(name)))
}

object Implicits {

  final implicit class FlowMetricsOps[In, Out, Mat](val flow: Flow[In, Out, Mat]) extends AnyVal {

    def checkpoint(name: String)(implicit backend: CheckpointBackend): Flow[In, Out, Mat] =
      flow.via(CheckpointStage(repository = backend.createRepository(name)))
  }

  final implicit class SourceMetricsOps[Out, Mat](val source: Source[Out, Mat]) extends AnyVal {

    def checkpoint(name: String)(implicit backend: CheckpointBackend): Source[Out, Mat] =
      source.via(CheckpointStage(repository = backend.createRepository(name)))
  }
}