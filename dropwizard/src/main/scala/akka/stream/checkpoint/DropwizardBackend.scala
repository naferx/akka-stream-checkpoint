package akka.stream.checkpoint

import com.codahale.metrics.MetricRegistry

object DropwizardBackend {

  implicit def fromRegistry(implicit metricRegistry: MetricRegistry): CheckpointBackend = new CheckpointBackend {
    override def createRepository(name: String): CheckpointRepository = DropwizardCheckpointRepository(name)
  }
}
