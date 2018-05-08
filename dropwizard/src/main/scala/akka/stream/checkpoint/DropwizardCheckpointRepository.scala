package akka.stream.checkpoint

import com.codahale.metrics.MetricRegistry

private[checkpoint] object DropwizardCheckpointRepository {

  def apply(name: String)(implicit metricRegistry: MetricRegistry): CheckpointRepository = new CheckpointRepository {

    private val pullLatency       = metricRegistry.histogram(name + "_pull_latency")
    private val pushLatency       = metricRegistry.histogram(name + "_push_latency")
    private val backpressureRatio = metricRegistry.histogram(name + "_backpressure_ratio")
    private val throughput        = metricRegistry.meter(name + "_throughput")
    private val backpressured     = metricRegistry.counter(name + "_backpressured")

    backpressured.inc()

    override def markPull(nanos: Long): Unit = {
      pullLatency.update(nanos)
      backpressured.dec()
    }

    override def markPush(nanos: Long, ratio: Long): Unit = {
      pushLatency.update(nanos)
      backpressureRatio.update(ratio)
      throughput.mark()
      backpressured.inc()
    }
  }
}