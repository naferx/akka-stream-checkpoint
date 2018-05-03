package akka.stream.checkpoint

trait CheckpointRepository {

  def addPullLatency(nanos: Long): Unit

  def addPushLatency(nanos: Long): Unit
}

trait CheckpointBackend {

  def createRepository(name: String): CheckpointRepository
}