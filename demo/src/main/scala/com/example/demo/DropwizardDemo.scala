package com.example.demo

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import akka.stream.checkpoint.{CheckpointBackend, DropwizardBackend}
import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, MetricRegistry}

trait DropwizardDemo extends CheckpointDemo {

  implicit val metricRegistry : MetricRegistry  = new MetricRegistry()

  implicit val backend: CheckpointBackend = DropwizardBackend.fromRegistry(metricRegistry)

  GraphiteReporter
    .forRegistry(metricRegistry)
    .prefixedWith("dropwizard")
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .filter(MetricFilter.ALL)
    .build(new Graphite(new InetSocketAddress("localhost", 2003)))
    .start(1, TimeUnit.SECONDS)
}

object DropwizardNoBackpressure  extends DropwizardDemo with NoBackpressure
object DropwizardBackpressureAB  extends DropwizardDemo with BackpressureAB
object DropwizardBackpressureBC  extends DropwizardDemo with BackpressureBC
object DropwizardBackpressureABC extends DropwizardDemo with BackpressureABC
object DropwizardFilter          extends DropwizardDemo with Filter
object DropwizardConflate        extends DropwizardDemo with Conflate
object DropwizardLiveness        extends DropwizardDemo with LivenessIssue