package eu.svez.stream.checkpoint

trait CheckpointRepository {

  def addPullLatency(nanos: Long): Unit

  def addPushLatency(nanos: Long): Unit
}