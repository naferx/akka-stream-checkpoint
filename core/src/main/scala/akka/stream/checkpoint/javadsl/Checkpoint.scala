package akka.stream.checkpoint.javadsl

import akka.NotUsed
import akka.stream.checkpoint.{CheckpointBackend, CheckpointStage}
import akka.stream.javadsl

object Checkpoint {

  def create[T](name: String, backend: CheckpointBackend): javadsl.Flow[T, T, NotUsed] =
    javadsl.Flow.fromGraph(CheckpointStage[T](repository = backend.createRepository(name)))

}
