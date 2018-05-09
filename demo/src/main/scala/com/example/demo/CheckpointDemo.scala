package com.example.demo

import akka.NotUsed
import akka.actor.ActorSystem
import akka.pattern.after
import akka.stream.ActorMaterializer
import akka.stream.checkpoint.CheckpointBackend
import akka.stream.checkpoint.scaladsl.Implicits._
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait CheckpointDemo extends App {

  implicit val backend: CheckpointBackend

  implicit val system: ActorSystem                = ActorSystem("stream-checkpoint-demo")
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer    = ActorMaterializer()

  def ioSimulation[T](duration: FiniteDuration)(n: T): Future[T] =
    if (duration == Duration.Zero)
      Future.successful(n)
    else
      after(duration, system.scheduler)(Future.successful(n))

  def slowIOSimulation[T] = ioSimulation[T](1.second)(_)
  def fastIOSimulation[T] = ioSimulation[T](10.millis)(_)

  def cpuSimulation[T](duration: FiniteDuration)(n: T): T = {
    val sleepTime = duration.toNanos
    val startTime = System.nanoTime
    while ((System.nanoTime - startTime) < sleepTime) {}
    n
  }
}

// fast - fast: no backpressure
trait NoBackpressure extends CheckpointDemo {
  Source.tick(1.second, 1.second, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .mapAsync(1)(fastIOSimulation)
    .checkpoint("B")
    .mapAsync(1)(fastIOSimulation)
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}

// slow - fast: backpressuring flow 1
trait BackpressureAB extends CheckpointDemo {
  Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .mapAsync(1)(slowIOSimulation)
    .checkpoint("B")
    .mapAsync(1)(fastIOSimulation)
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}

// fast - slow: backpressuring flow 2
trait BackpressureBC extends CheckpointDemo {
  Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .mapAsync(1)(fastIOSimulation)
    .checkpoint("B")
    .mapAsync(1)(slowIOSimulation)
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}

// slow - slow: backpressuring flow 2 and 3
trait BackpressureABC extends CheckpointDemo {
  Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .mapAsync(1)(slowIOSimulation)
    .checkpoint("B")
    .mapAsync(1)(slowIOSimulation)
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}

// different throughputs between A and B
trait Filter extends CheckpointDemo {
  Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .filter(_ % 2 != 0)
    .checkpoint("B")
    .mapAsync(1)(slowIOSimulation)
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}

// no backpressure on A, as events are being consumed and conflated
trait Conflate extends CheckpointDemo {
  Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .conflate(_ + _)
    .checkpoint("B")
    .mapAsync(1)(slowIOSimulation)
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}

// liveness issue simulation
trait LivenessIssue extends CheckpointDemo {
  Source.tick(100.millis, 100.millis, NotUsed).zipWithIndex.map(_._2)
    .checkpoint("A")
    .mapAsync(1)(fastIOSimulation)
    .checkpoint("B")
    .mapAsync(1)(x ⇒ if (x < 100) fastIOSimulation(x) else ioSimulation(1.hour)(x))
    .checkpoint("C")
    .runWith(Sink.foreach(println))
}