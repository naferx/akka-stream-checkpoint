package akka.stream.checkpoint

import com.codahale.metrics.MetricRegistry
import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.duration._

class DropwizardCheckpointRepositorySpec extends WordSpec with MustMatchers {

  "DropwizardCheckpointRepository" should {

    val registry = new MetricRegistry()
    val repository = DropwizardCheckpointRepository("test")(registry)

    "create two timers for pull and push latencies" in {
      registry.getTimers must contain key "test_pull"
      registry.getTimers must contain key "test_push"
    }

    "store the metrics in aptly named histograms" when {

      "pull latencies are added" in {
        val latency = 1.milli.toNanos
        repository.addPullLatency(latency)

        registry.timer("test_pull").getCount must ===(1)
        registry.timer("test_pull").getSnapshot.getValues must ===(Array(latency))
      }

      "push latencies are added" in {
        val latency = 5.millis.toNanos
        repository.addPushLatency(latency)

        registry.timer("test_push").getCount must ===(1)
        registry.timer("test_push").getSnapshot.getValues must ===(Array(latency))
      }
    }
  }
}
