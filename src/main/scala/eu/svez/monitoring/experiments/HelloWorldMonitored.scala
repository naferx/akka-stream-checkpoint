package eu.svez.monitoring.experiments

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.pattern.after
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.codahale.metrics.MetricRegistry

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import com.codahale.metrics.MetricFilter
import com.codahale.metrics.graphite.Graphite
import com.codahale.metrics.graphite.GraphiteReporter
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import eu.svez.monitoring.experiments.HelloWorldMonitored.ioSimulation

object HelloWorldMonitored extends App {

  implicit val system: ActorSystem                = ActorSystem("stream-demo")
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(system).withInputBuffer(initialSize = 1, maxSize = 1)
  )

  def ioSimulation(duration: FiniteDuration)(n: Long): Future[Long] =
    if (duration == Duration.Zero)
      Future.successful(n)
    else
      after(duration, system.scheduler)(Future.successful(n))

  def slowIOSimulation = ioSimulation(1.second)(_)
  def fastIOSimulation = ioSimulation(10.millis)(_)

  def cpuSimulation(duration: FiniteDuration)(n: Long): Long = {
    val sleepTime = duration.toNanos
    val startTime = System.nanoTime
    while ((System.nanoTime - startTime) < sleepTime) {}
    n
  }

  implicit val metricRegistry: MetricRegistry = new MetricRegistry()

  GraphiteReporter
    .forRegistry(metricRegistry)
    .prefixedWith("dropwizard")
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .filter(MetricFilter.ALL)
    .build(new Graphite(new InetSocketAddress("localhost", 2003)))
    .start(1, TimeUnit.SECONDS)

  haltingStream

  // fast - fast: no backpressure
  def noBackpressure: Future[Done] =
    Source.tick(1.second, 1.second, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .mapAsync(1)(fastIOSimulation)
      .via(Checkpoint("B"))
      .mapAsync(1)(fastIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  // slow - fast: backpressuring flow 1
  def backpressuringAB: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("B"))
      .mapAsync(1)(fastIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  // fast - slow: backpressuring flow 2
  def backpressuringBC: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .mapAsync(1)(fastIOSimulation)
      .via(Checkpoint("B"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  // slow - slow: backpressuring flow 2 and 3
  def backpressuringABC: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("B"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  // now A is not backpressuring anymore, as events are being consumed and conflated
  def backpressuringWithConflating: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .conflate(_ + _)
      .via(Checkpoint("B"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  def backpressuringWithExpanding: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("B"))
      .expand(List(_).iterator)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  def backpressuringWithFiltering: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .filter(_ % 2 != 0)
      .via(Checkpoint("B"))
      .mapAsync(1)(slowIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))

  def haltingStream: Future[Done] =
    Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
      .via(Checkpoint("A"))
      .mapAsync(1)(x ⇒ if (x < 100) fastIOSimulation(x) else ioSimulation(1.hour)(x))
      .via(Checkpoint("B"))
      .mapAsync(1)(fastIOSimulation)
      .via(Checkpoint("C"))
      .runWith(Sink.foreach(println))



}

