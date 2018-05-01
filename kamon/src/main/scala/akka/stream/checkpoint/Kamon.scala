package akka.stream.checkpoint

import kamon.metric.MeasurementUnit

object Kamon {

  implicit val factory: CheckpointRepositoryFactory = new CheckpointRepositoryFactory {
    override def createRepository(name: String): CheckpointRepository = forName(name)
  }

  private[checkpoint] def forName(name: String) = new CheckpointRepository {
    private val pullHistogram = kamon.Kamon.histogram(name + "_pull", MeasurementUnit.time.nanoseconds)
    private val pushHistogram = kamon.Kamon.histogram(name + "_push", MeasurementUnit.time.nanoseconds)

    override def addPullLatency(nanos: Long): Unit = pullHistogram.record(nanos)
    override def addPushLatency(nanos: Long): Unit = pushHistogram.record(nanos)
  }

}
