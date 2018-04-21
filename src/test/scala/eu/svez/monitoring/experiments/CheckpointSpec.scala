package eu.svez.monitoring.experiments

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpec}

class CheckpointSpec extends WordSpec with MustMatchers with ScalaFutures {

  implicit val system = ActorSystem("CheckpointSpec")
  implicit val materializer = ActorMaterializer()

  implicit val noopCheckpoints: String ⇒ CheckpointRepository = _ ⇒ new CheckpointRepository {
    override def addPushLatency(nanos: Long): Unit = ()
    override def addPullLatency(nanos: Long): Unit = ()
  }

  "The Checkpoint stage" should {
    "be a pass-through stage" in {
      val values = 1 to 5

      val results = Source(1 to 5).via(Checkpoint("my_checkpoint")).runWith(Sink.seq).futureValue

      results must ===(values)
    }
  }

}
