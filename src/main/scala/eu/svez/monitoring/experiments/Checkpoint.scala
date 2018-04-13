package eu.svez.monitoring.experiments

import java.util.concurrent.TimeUnit

import akka.stream.ActorAttributes.SupervisionStrategy
import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import com.codahale.metrics.MetricRegistry

import scala.util.control.NonFatal

final case class Checkpoint[T](label: String)(implicit metricRegistry: MetricRegistry) extends GraphStage[FlowShape[T, T]] {
  val in = Inlet[T]("Checkpoint.in")
  val out = Outlet[T]("Checkpoint.out")
  override val shape = FlowShape(in, out)

  private val pullLatency = metricRegistry.timer(label + "_pull")
  private val pushLatency = metricRegistry.timer(label + "_push")

  override def initialAttributes: Attributes = Attributes.name("checkpoint")

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with InHandler with OutHandler {

      var lastPulled: Long = System.nanoTime()
      var lastPushed: Long = lastPulled

      private def decider =
        inheritedAttributes.get[SupervisionStrategy].map(_.decider).getOrElse(Supervision.stoppingDecider)

      override def onPush(): Unit = {
        try {
          push(out, grab(in))

          lastPushed = System.nanoTime()
          pushLatency.update(lastPushed - lastPulled, TimeUnit.NANOSECONDS)
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
        pullLatency.update(lastPulled - lastPushed, TimeUnit.NANOSECONDS)
      }

      setHandlers(in, out, this)
    }
}
