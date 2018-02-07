package eu.svez.monitoring.experiments

trait MetricsBackend {

  def markPush(): Unit

  def markPull(): Unit

  def registerLatency(latency: Long): Unit
}

final class ConsoleBackend extends MetricsBackend {

  override def markPush(): Unit = ???

  override def markPull(): Unit = ???

  override def registerLatency(latency: Long): Unit = ???
}