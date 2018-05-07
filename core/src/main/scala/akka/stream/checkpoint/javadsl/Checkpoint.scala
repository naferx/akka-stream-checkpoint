package akka.stream.checkpoint.javadsl

import akka.NotUsed
import akka.stream.checkpoint.{CheckpointBackend, CheckpointStage}
import akka.stream.javadsl

object Checkpoint {

  /**
    * Java API
    * Creates a checkpoint Flow.
    *
    * @param name checkpoint identification label
    * @param backend backend to store the checkpoint readings
    * @tparam T pass-through type of the elements that will flow through the checkpoint
    * @return a newly created checkpoint Flow
    */
  def create[T](name: String, backend: CheckpointBackend): javadsl.Flow[T, T, NotUsed] =
    javadsl.Flow.fromGraph(CheckpointStage[T](repository = backend.createRepository(name)))

}
