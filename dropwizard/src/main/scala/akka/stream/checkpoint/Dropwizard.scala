package akka.stream.checkpoint

import com.codahale.metrics.MetricRegistry
import java.util.concurrent.TimeUnit.NANOSECONDS

object Dropwizard {

  implicit def factory(implicit metricRegistry: MetricRegistry): CheckpointRepositoryFactory = new CheckpointRepositoryFactory {
    override def createRepository(name: String): CheckpointRepository = forName(name)
  }

  private[checkpoint] def forName(name: String)(implicit metricRegistry: MetricRegistry) = new CheckpointRepository {
    private val pullTimer = metricRegistry.timer(name + "_pull")
    private val pushTimer = metricRegistry.timer(name + "_push")

    override def addPullLatency(nanos: Long): Unit = pullTimer.update(nanos, NANOSECONDS)
    override def addPushLatency(nanos: Long): Unit = pushTimer.update(nanos, NANOSECONDS)
  }

}