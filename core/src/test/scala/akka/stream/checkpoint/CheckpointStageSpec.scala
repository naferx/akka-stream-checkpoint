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

      implicit val noopCheckpoints: String ⇒ CheckpointRepository = _ ⇒ new CheckpointRepository {
        override def addPushLatency(nanos: Long): Unit = ()
        override def addPullLatency(nanos: Long): Unit = ()
      }

      val values = 1 to 5

      val results = Source(values).via(CheckpointStage("my_checkpoint")).runWith(Sink.seq).futureValue

      results must ===(values)
    }

    "record latencies between pulls and pushes" in {
      val pullLatency = 100.millis
      val pushLatency = 300.millis

      val pushLatencies = ListBuffer.empty[Long]
      val pullLatencies = ListBuffer.empty[Long]

      implicit val arrayBackedCheckpoints: String ⇒ CheckpointRepository = _ ⇒ new CheckpointRepository {
        override def addPushLatency(nanos: Long): Unit = pushLatencies += nanos
        override def addPullLatency(nanos: Long): Unit = pullLatencies += nanos
      }

      val (sourcePromise, probe) =
        Source.maybe[Int]
          .via(CheckpointStage("my_checkpoint"))
          .toMat(TestSink.probe[Int])(Keep.both)
          .run()

      pullLatencies.size must ===(0)
      pushLatencies.size must ===(0)

      Thread.sleep(pullLatency.toMillis)
      probe.request(1)

      eventually {
        pullLatencies.size must ===(1)
        pushLatencies.size must ===(0)

        pullLatencies.head must be > pullLatency.toNanos
      }

      Thread.sleep(pushLatency.toMillis)
      sourcePromise.success(Some(42))

      eventually {
        pullLatencies.size must ===(1)
        pushLatencies.size must ===(1)

        pushLatencies.head must be > pushLatency.toNanos
      }

      probe.expectNext(42)
    }
  }

}
