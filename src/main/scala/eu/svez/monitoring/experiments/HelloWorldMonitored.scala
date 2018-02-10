package eu.svez.monitoring.experiments

import akka.NotUsed
import akka.actor.ActorSystem
import akka.pattern.after
import akka.stream.actor.ActorPublisher
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Attributes, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}
import kamon.Kamon

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object HelloWorldMonitored extends App {

  implicit val system: ActorSystem                = ActorSystem("stream-demo")
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(system).withInputBuffer(initialSize = 1, maxSize = 1)
  )

  Kamon.start()

  scala.sys.addShutdownHook {
    Kamon.shutdown()
    system.terminate()
  }

  def ioSimulation(duration: FiniteDuration)(n: Long): Future[Long] =
    after(duration, system.scheduler)(Future.successful(n))

  def cpuSimulation(duration: FiniteDuration)(n: Long): Long = {
    val sleepTime = duration.toNanos
    val startTime = System.nanoTime
    while ((System.nanoTime - startTime) < sleepTime) {}
    n
  }

  Source.tick(100.millis, 1.millis, NotUsed).zipWithIndex.map(_._2)
    .via(Checkpoint("A"))
    .mapAsync(1)(x ⇒ if (x == 15) ioSimulation(1.day)(x) else ioSimulation(1.second)(x))
    .via(Checkpoint("B"))
    .runWith(Sink.foreach(println))

//  Source.tick(1.second, 1.second, NotUsed).zipWithIndex.map(_._2)
//    .via(Checkpoint("A"))
//    .filter(_ % 2 == 0)
//    .via(Checkpoint("B"))
//    .runWith(Sink.foreach(println))

//  Source.tick(1.second, 1.second, NotUsed).zipWithIndex.map(_._2)
//    .via(Checkpoint("A"))
//    .mapConcat(x ⇒ List(x, x + 0.5))
//    .via(Checkpoint("B"))
//    .runWith(Sink.foreach(println))

}

