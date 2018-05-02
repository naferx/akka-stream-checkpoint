package akka.stream.checkpoint.javadsl

import akka.NotUsed
import akka.stream.checkpoint.{CheckpointRepositoryFactory, CheckpointStage}
import akka.stream.javadsl

object Checkpoint {

  def create[T](name: String, repositoryFactory: CheckpointRepositoryFactory): javadsl.Flow[T, T, NotUsed] =
    javadsl.Flow.fromGraph(CheckpointStage[T](repository = repositoryFactory.createRepository(name)))

}
