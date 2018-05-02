package akka.stream.checkpoint

import akka.stream.ActorAttributes.SupervisionStrategy
import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import scala.util.control.NonFatal

private[checkpoint] final case class CheckpointStage[T](repository: CheckpointRepository) extends GraphStage[FlowShape[T, T]] {
  val in = Inlet[T]("Checkpoint.in")
  val out = Outlet[T]("Checkpoint.out")
  override val shape = FlowShape(in, out)

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

          val now = System.nanoTime()
          repository.markPush(now - lastPulled, BigDecimal(lastPulled - lastPushed) / (now - lastPushed))
          lastPushed = now
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
        repository.markPull(lastPulled - lastPushed)
      }

      setHandlers(in, out, this)
    }
}
