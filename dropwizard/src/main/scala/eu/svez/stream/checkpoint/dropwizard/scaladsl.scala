package eu.svez.stream.checkpoint.dropwizard

import akka.stream.scaladsl.{Flow, Source}
import com.codahale.metrics.MetricRegistry
import eu.svez.stream.checkpoint.Checkpoint

object scaladsl {

  final implicit class FlowMetricsOps[In, Out, Mat](val flow: Flow[In, Out, Mat]) extends AnyVal {

    def checkpoint(name: String)(implicit metricRegistry: MetricRegistry): Flow[In, Out, Mat] =
      flow.via(Checkpoint(repository = DropwizardCheckpointRepository(name)))
  }

  final implicit class SourceMetricsOps[Out, Mat](val source: Source[Out, Mat]) extends AnyVal {

    def checkpoint(name: String)(implicit metricRegistry: MetricRegistry): Source[Out, Mat] =
      source.via(Checkpoint(repository = DropwizardCheckpointRepository(name)))

  }
}