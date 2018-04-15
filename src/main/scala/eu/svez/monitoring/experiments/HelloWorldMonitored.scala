package eu.svez.monitoring.experiments

import java.lang
import java.time.Instant
import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.Control
import akka.pattern.after
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import kamon.Kamon
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.{LongDeserializer, StringDeserializer}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

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
//
//  Source.tick(100.millis, 1.millis, NotUsed).zipWithIndex.map(_._2)
//    .via(Checkpoint("A"))
//    .filter(_ % 2 == 0)
//    .via(Checkpoint("B"))
//    .runWith(Sink.foreach(println))

  val kafkaSrc: Source[ConsumerRecord[String, lang.Long], Control] =
    Consumer.plainSource(
      ConsumerSettings(system, new StringDeserializer, new LongDeserializer)
        .withBootstrapServers("localhost:29092")
        .withGroupId("group1")
        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"),
      Subscriptions.topics("input-test")
    )

  kafkaSrc.runForeach(println)

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

