package akka.stream.checkpoint

import kamon.metric.MeasurementUnit

private[checkpoint] object KamonCheckpointRepository {

  def apply(name: String): CheckpointRepository = new CheckpointRepository {
    private val pullHistogram = kamon.Kamon.histogram(name + "_pull_latency", MeasurementUnit.time.nanoseconds)
    private val pushHistogram = kamon.Kamon.histogram(name + "_push_latency", MeasurementUnit.time.nanoseconds)

    private val pullCounter = kamon.Kamon.counter(name + "_pull_rate")
    private val pushCounter = kamon.Kamon.counter(name + "_push_rate")

    override def addPullLatency(nanos: Long): Unit = {
      pullHistogram.record(nanos)
      pullCounter.increment()
    }
    override def addPushLatency(nanos: Long): Unit = {
      pushHistogram.record(nanos)
      pushCounter.increment()
    }
  }
}