package eu.svez.monitoring.experiments

import java.util.concurrent.TimeUnit

import akka.stream.ActorAttributes.SupervisionStrategy
import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import com.codahale.metrics.MetricRegistry

import scala.util.control.NonFatal

trait CheckpointRepository {
  def addPullLatency(nanos: Long): Unit
  def addPushLatency(nanos: Long): Unit
}

object DropwizardCheckpointSupport {
  implicit def withRegistry(implicit metricRegistry: MetricRegistry): String ⇒ CheckpointRepository = name ⇒ new CheckpointRepository {
    private val pullTimer = metricRegistry.timer(name + "_pull")
    private val pushTimer = metricRegistry.timer(name + "_push")

    override def addPullLatency(nanos: Long): Unit = pullTimer.update(nanos, TimeUnit.NANOSECONDS)
    override def addPushLatency(nanos: Long): Unit = pushTimer.update(nanos, TimeUnit.NANOSECONDS)
  }
}

final case class Checkpoint[T](name: String)(implicit backendFor: String ⇒ CheckpointRepository) extends GraphStage[FlowShape[T, T]] {
  val in = Inlet[T]("Checkpoint.in")
  val out = Outlet[T]("Checkpoint.out")
  override val shape = FlowShape(in, out)

  private val backend = backendFor(name)

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
          backend.addPushLatency(lastPushed - lastPulled)
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
        backend.addPullLatency(lastPulled - lastPushed)
      }

      setHandlers(in, out, this)
    }
}
