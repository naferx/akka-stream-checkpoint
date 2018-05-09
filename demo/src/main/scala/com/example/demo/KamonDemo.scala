package com.example.demo

import akka.stream.checkpoint.{CheckpointBackend, KamonBackend}

trait KamonDemo extends CheckpointDemo {

  implicit val backend: CheckpointBackend = KamonBackend.instance

  kamon.Kamon.addReporter(new kamon.statsd.StatsDReporter())
}

object KamonNoBackpressure  extends KamonDemo with NoBackpressure
object KamonBackpressureAB  extends KamonDemo with BackpressureAB
object KamonBackpressureBC  extends KamonDemo with BackpressureBC
object KamonBackpressureABC extends KamonDemo with BackpressureABC
object KamonFilter          extends KamonDemo with Filter
object KamonConflate        extends KamonDemo with Conflate
object KamonLiveness        extends KamonDemo with LivenessIssue