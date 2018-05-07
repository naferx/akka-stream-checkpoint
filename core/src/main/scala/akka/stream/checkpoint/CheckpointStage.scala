package akka.stream.checkpoint

import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

private[checkpoint] final case class CheckpointStage[T](repository: CheckpointRepository) extends GraphStage[FlowShape[T, T]] {
  val in = Inlet[T]("Checkpoint.in")
  val out = Outlet[T]("Checkpoint.out")
  override val shape = FlowShape(in, out)

  override def initialAttributes: Attributes = Attributes.name("checkpoint")

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with InHandler with OutHandler {

      var lastPulled: Long = 0L
      var lastPushed: Long = 0L

      override def preStart(): Unit = {
        lastPulled = System.nanoTime()
        lastPushed = lastPulled
      }

      override def onPush(): Unit = {
        push(out, grab(in))

        val now = System.nanoTime()
        repository.markPush(now - lastPulled, (lastPulled - lastPushed) * 100 / (now - lastPushed))
        lastPushed = now
      }

      override def onPull(): Unit = {
        pull(in)

        lastPulled = System.nanoTime()
        repository.markPull(lastPulled - lastPushed)
      }

      setHandlers(in, out, this)
    }
}
