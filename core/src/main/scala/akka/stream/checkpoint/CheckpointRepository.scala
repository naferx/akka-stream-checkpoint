package akka.stream.checkpoint

/**
  * Represents a storage interface to allow a single checkpoint to store its readings
  */
trait CheckpointRepository {

  /**
    * Allows to store readings following a pull signal
    * @param latencyNanos latency between the last push signal and the registered pull signal in nanoseconds
    */
  def markPull(latencyNanos: Long): Unit

  /**
    * Allows to store readings following a push signal
    * @param latencyNanos latency between the last pull signal and the registered push signal in nanoseconds
    * @param backpressureRatio ratio between the pull latency and the entire last push-push cycle
    */
  def markPush(latencyNanos: Long, backpressureRatio: BigDecimal): Unit
}

/**
  * Factory to create checkpoint repositories. Represents a storage interface to allow
  * multiple checkpoints to store their readings
  */
trait CheckpointBackend {

  def createRepository(name: String): CheckpointRepository
}