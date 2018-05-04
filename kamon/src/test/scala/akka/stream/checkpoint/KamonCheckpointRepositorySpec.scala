package akka.stream.checkpoint

import kamon.Kamon
import kamon.testkit.MetricInspection
import org.scalatest.{MustMatchers, WordSpec}

class KamonCheckpointRepositorySpec extends WordSpec with MustMatchers with MetricInspection {

  "KamonCheckpointRepository" should {

    val repository = KamonCheckpointRepository("test")

    "store readings in aptly named metrics" when {

      "elements are pulled into the checkpoint" in {
        val latency = 42L
        repository.markPull(latency)

        val distribution = Kamon.histogram("test_pull_latency").distribution()
        distribution.count must ===(1)
        distribution.max   must ===(latency)
      }

      "elements are pushed through the checkpoint" in {
        val latency = 64L
        val backpressureRatio = 0.54
        repository.markPush(latency, backpressureRatio)

        val latencyDistro = Kamon.histogram("test_push_latency").distribution()
        latencyDistro.count must ===(1)
        latencyDistro.max   must ===(latency)

        val backpressureDistro = Kamon.histogram("test_backpressure_ratio").distribution()
        backpressureDistro.count must ===(1)
        backpressureDistro.max   must ===(backpressureRatio * 100)

        Kamon.counter("test_throughput").value().longValue() must ===(1)
      }
    }
  }
}
