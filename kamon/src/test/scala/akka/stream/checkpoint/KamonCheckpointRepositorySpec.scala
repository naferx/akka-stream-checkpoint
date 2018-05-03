package akka.stream.checkpoint

import kamon.testkit.MetricInspection
import org.scalatest.{MustMatchers, WordSpec}

class KamonCheckpointRepositorySpec extends WordSpec with MustMatchers with MetricInspection {

  "KamonCheckpointRepository" should {

    val repository = KamonCheckpointRepository("test")

    "store the metrics in aptly named histograms" when {

      "pull latencies are added" in {
        val latency = 42L
        repository.addPullLatency(latency)

        val distribution = kamon.Kamon.histogram("test_pull_latency").distribution()
        distribution.count must ===(1)
        distribution.max   must ===(latency)

        kamon.Kamon.counter("test_pull_rate").value().longValue() must ===(1)
      }

      "push latencies are added" in {
        val latency = 64L
        repository.addPushLatency(latency)

        val distribution = kamon.Kamon.histogram("test_push_latency").distribution()
        distribution.count must ===(1)
        distribution.max   must ===(latency)

        kamon.Kamon.counter("test_push_rate").value().longValue() must ===(1)
      }
    }
  }
}
