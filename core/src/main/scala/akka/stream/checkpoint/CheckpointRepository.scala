package akka.stream.checkpoint

trait CheckpointRepository {

  def markPull(latencyNanos: Long): Unit

  def markPush(latencyNanos: Long, backpressureRatio: BigDecimal): Unit
}

trait CheckpointBackend {

  def createRepository(name: String): CheckpointRepository
}