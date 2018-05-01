package akka.stream.checkpoint.dropwizard

import java.util.concurrent.TimeUnit.NANOSECONDS

import akka.stream.checkpoint.CheckpointRepository
import com.codahale.metrics.MetricRegistry

private[checkpoint] object DropwizardCheckpointRepository {

  def apply(name: String)(implicit metricRegistry: MetricRegistry): CheckpointRepository = new CheckpointRepository {

    private val pullTimer = metricRegistry.timer(name + "_pull")
    private val pushTimer = metricRegistry.timer(name + "_push")

    override def addPullLatency(nanos: Long): Unit = pullTimer.update(nanos, NANOSECONDS)
    override def addPushLatency(nanos: Long): Unit = pushTimer.update(nanos, NANOSECONDS)
  }
}
