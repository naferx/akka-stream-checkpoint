package akka.stream.checkpoint

trait CheckpointRepository {

  def addPullLatency(nanos: Long): Unit

  def addPushLatency(nanos: Long): Unit
}

trait CheckpointRepositoryFactory {

  def createRepository(name: String): CheckpointRepository
}