package eu.svez.monitoring.experiments

import akka.stream.ActorAttributes.SupervisionStrategy
import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import kamon.Kamon

import scala.util.control.NonFatal

final case class Checkpoint[T](label: String) extends GraphStage[FlowShape[T, T]] {
  val in = Inlet[T]("Checkpoint.in")
  val out = Outlet[T]("Checkpoint.out")
  override val shape = FlowShape(in, out)

  private val pullCounter      = Kamon.metrics.counter(label + "_pull")
  private val pushCounter      = Kamon.metrics.counter(label + "_push")
  private val statusCounter    = Kamon.metrics.minMaxCounter(label + "_status")
  private val latencyHistogram = Kamon.metrics.histogram(label + "_latency")

  override def initialAttributes: Attributes = Attributes.name("checkpoint")

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with InHandler with OutHandler {

      var lastPulled: Long = _

      private def decider =
        inheritedAttributes.get[SupervisionStrategy].map(_.decider).getOrElse(Supervision.stoppingDecider)

      override def onPush(): Unit = {
        try {
          push(out, grab(in))

          val latency = System.nanoTime() - lastPulled
          latencyHistogram.record(latency)
          pushCounter.increment()
          statusCounter.decrement()
        } catch {
          case NonFatal(ex) ⇒ decider(ex) match {
            case Supervision.Stop ⇒ failStage(ex)
            case _                ⇒ pull(in)
          }
        }
      }

      override def onPull(): Unit = {
        pull(in)

        lastPulled = System.nanoTime()
        pullCounter.increment()
        statusCounter.increment()
      }

      setHandlers(in, out, this)
    }
}
