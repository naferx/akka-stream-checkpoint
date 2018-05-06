package akka.stream.checkpoint

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.testkit.scaladsl.TestSink
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{MustMatchers, WordSpec}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._

class CheckpointStageSpec extends WordSpec with MustMatchers with ScalaFutures with Eventually {

  implicit val system = ActorSystem("CheckpointSpec")
  implicit val materializer = ActorMaterializer()

  "The Checkpoint stage" should {
    "be a pass-through stage" in {

      val noopRepo = new CheckpointRepository {
        override def markPull(latencyNanos: Long): Unit = ()
        override def markPush(latencyNanos: Long, backpressureRatio: BigDecimal): Unit = ()
      }

      val values = 1 to 5

      val results = Source(values).via(CheckpointStage(noopRepo)).runWith(Sink.seq).futureValue

      results must ===(values)
    }

    "record latencies between pulls and pushes" in {
      val pullLatency = 400.millis
      val pushLatency = 300.millis

      val tolerance = 50.millis

      val pushLatencies = ListBuffer.empty[Long]
      val pullLatencies = ListBuffer.empty[Long]

      val arrayBackedRepo = new CheckpointRepository {
        override def markPush(nanos: Long, backpressureRatio: BigDecimal): Unit = pushLatencies += nanos
        override def markPull(nanos: Long): Unit = pullLatencies += nanos
      }

      val (sourcePromise, probe) =
        Source.maybe[Int]
          .via(CheckpointStage(arrayBackedRepo))
          .toMat(TestSink.probe[Int])(Keep.both)
          .run()

      pullLatencies.size must ===(0)
      pushLatencies.size must ===(0)

      Thread.sleep(pullLatency.toMillis)
      probe.request(1)

      eventually {
        pullLatencies.size must ===(1)
        pushLatencies.size must ===(0)

        pullLatencies.head must ===(pullLatency.toNanos +- tolerance.toNanos)
      }

      Thread.sleep(pushLatency.toMillis)
      sourcePromise.success(Some(42))

      eventually {
        pullLatencies.size must ===(1)
        pushLatencies.size must ===(1)

        pushLatencies.head must ===(pushLatency.toNanos +- tolerance.toNanos)
      }

      probe.expectNext(42)
    }

    "record backpressure ratios at push time" in {
      val pullLatency = 100.millis
      val pushLatency = 900.millis

      val expectedRatio = BigDecimal(0.1)
      val tolerance     = 0.01

      val backpressureRatios = ListBuffer.empty[BigDecimal]

      val arrayBackedRepo = new CheckpointRepository {
        override def markPush(nanos: Long, backpressureRatio: BigDecimal): Unit = backpressureRatios += backpressureRatio
        override def markPull(nanos: Long): Unit = ()
      }

      val (sourcePromise, probe) =
        Source.maybe[Int]
          .via(CheckpointStage(arrayBackedRepo))
          .toMat(TestSink.probe[Int])(Keep.both)
          .run()

      Thread.sleep(pullLatency.toMillis)
      probe.request(1)

      Thread.sleep(pushLatency.toMillis)
      sourcePromise.success(Some(42))

      eventually {
        backpressureRatios.size must ===(1)
        backpressureRatios.head must ===(expectedRatio +- tolerance)
      }
    }
  }

}
