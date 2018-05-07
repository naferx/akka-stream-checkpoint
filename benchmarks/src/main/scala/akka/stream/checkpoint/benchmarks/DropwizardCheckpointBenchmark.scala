package akka.stream.checkpoint.benchmarks

import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.checkpoint.DropwizardBackend._
import akka.stream.checkpoint.scaladsl.Checkpoint
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import com.codahale.metrics.MetricRegistry
import org.openjdk.jmh.annotations._

import scala.concurrent._
import scala.concurrent.duration._

/*
[info] Benchmark                                                         (numberOfFlows)  (repositoryType)   Mode  Cnt     Score     Error   Units
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements                1              none  thrpt   20  9181.100 ± 196.352  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements                1              sync  thrpt   20   643.488 ±  99.825  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements                1             async  thrpt   20   228.949 ±  17.520  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements                5              none  thrpt   20  2831.076 ± 107.756  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements                5              sync  thrpt   20   134.821 ±   9.241  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements                5             async  thrpt   20   189.149 ±  17.454  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements               20              none  thrpt   20   797.517 ±  27.768  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements               20              sync  thrpt   20    33.547 ±   0.663  ops/ms
[info] DropwizardCheckpointBenchmark.map_with_checkpoints_100k_elements               20             async  thrpt   20    51.590 ±   5.496  ops/ms
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class DropwizardCheckpointBenchmark {

  implicit val system = ActorSystem("DropwizardCheckpointBenchmark")
  implicit val materializer = ActorMaterializer()
  implicit val ctx = system.dispatcher

  implicit val metricRegistry = new MetricRegistry()

  val numberOfElements = 100000

  @Param(Array("1", "5", "20"))
  var numberOfFlows = 1

  @Param(Array("none", "sync", "async"))
  var repositoryType = "none"

  var graph: RunnableGraph[Future[Done]] = _

  @Setup
  def setup(): Unit = {
    val source = Source.repeat(1).take(numberOfElements)
    val flow = Flow[Int].map(_ + 1)

    def stage(n: Int) = repositoryType match {
      case "none" ⇒ flow.via(Flow.fromFunction(identity))
      case "sync" ⇒ flow.via(Checkpoint[Int](n.toString))
      case "async" ⇒ flow.via(Checkpoint[Int](n.toString)).async
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
