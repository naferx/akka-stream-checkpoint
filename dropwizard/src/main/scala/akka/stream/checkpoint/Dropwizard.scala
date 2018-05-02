package akka.stream.checkpoint

import com.codahale.metrics.MetricRegistry

object Dropwizard {

  implicit def factory(implicit metricRegistry: MetricRegistry): CheckpointRepositoryFactory = new CheckpointRepositoryFactory {
    override def createRepository(name: String): CheckpointRepository = DropwizardCheckpointRepository(name)
  }
}
