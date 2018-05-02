package akka.stream.checkpoint

import kamon.Kamon
import kamon.metric.MeasurementUnit

private[checkpoint] object KamonCheckpointRepository {

  def apply(name: String): CheckpointRepository = new CheckpointRepository {
    private val pullLatency       = Kamon.histogram(name + "_pull_latency", MeasurementUnit.time.nanoseconds)
    private val pushLatency       = Kamon.histogram(name + "_push_latency", MeasurementUnit.time.nanoseconds)
    private val backpressureRatio = Kamon.histogram(name + "_backpressure_ratio", MeasurementUnit.time.nanoseconds)
    private val throughput        = Kamon.counter(name + "_throughput")

    override def markPull(nanos: Long): Unit = pullLatency.record(nanos)

    override def markPush(nanos: Long, ratio: BigDecimal): Unit = {
      pushLatency.record(nanos)
      backpressureRatio.record((ratio * 100).longValue)
      throughput.increment()
    }
  }
}