package eu.svez.stream.checkpoint.benchmarks

import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import com.codahale.metrics.MetricRegistry
import eu.svez.stream.checkpoint.Checkpoint
import eu.svez.stream.checkpoint.dropwizard.DropwizardCheckpointRepository
import org.openjdk.jmh.annotations._

import scala.concurrent._
import scala.concurrent.duration._

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class DropwizardCheckpointBenchmark {

  implicit val system = ActorSystem("DropwizardCheckpointBenchmark")
  implicit val materializer = ActorMaterializer()
  implicit val ctx = system.dispatcher

  implicit val metricRegistry = new MetricRegistry()

  val numberOfElements = 100000

  @Param(Array("1", "5"))
  var numberOfFlows = 1

  @Param(Array("none", "sync"))
  var repositoryType = "none"

  var graph: RunnableGraph[Future[Done]] = _

  @Setup
  def setup(): Unit = {
    val source = Source.repeat(1).take(numberOfElements)
    val flow = Flow[Int].map(_ + 1)

    def stage(n: Int) = repositoryType match {
      case "none" ⇒ flow
      case "sync" ⇒ flow.via(Checkpoint[Int](DropwizardCheckpointRepository(n.toString)))
    }

    val flows = (1 to numberOfFlows).map(stage).reduce(_ via _)
    graph = source.via(flows).toMat(Sink.ignore)(Keep.right)
  }

  @TearDown
  def shutdown(): Unit = {
    Await.result(system.terminate(), 5.seconds)
  }

  @Benchmark
  @OperationsPerInvocation(100000) // Note: needs to match NumberOfElements.
  def map_with_checkpoints_100k_elements(): Unit = {
    Await.result(graph.run(), Duration.Inf)
  }
}
