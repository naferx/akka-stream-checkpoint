package akka.stream.checkpoint.demos

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import akka.stream.checkpoint.{CheckpointDemo, CheckpointRepositoryFactory, Dropwizard}
import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, MetricRegistry}

object DropwizardDemo extends CheckpointDemo {

  implicit val metricRegistry : MetricRegistry  = new MetricRegistry()

  implicit val repositoryFactory: CheckpointRepositoryFactory = Dropwizard.factory

  GraphiteReporter
    .forRegistry(metricRegistry)
    .prefixedWith("dropwizard")
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .filter(MetricFilter.ALL)
    .build(new Graphite(new InetSocketAddress("localhost", 2003)))
    .start(1, TimeUnit.SECONDS)

  backpressuringAB
}