package com.example;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.checkpoint.CheckpointRepositoryFactory;
import akka.stream.checkpoint.Dropwizard;
import akka.stream.checkpoint.javadsl.Checkpoint;
import akka.stream.javadsl.Source;
import com.codahale.metrics.MetricRegistry;

public class CheckpointJavaExample {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("stream-checkpoint-java-demo");
        final Materializer materializer = ActorMaterializer.create(system);

        final MetricRegistry metricRegistry = new MetricRegistry();
        final CheckpointRepositoryFactory factory = Dropwizard.factory(metricRegistry);

        Source.range(1, 100)
            .via(Checkpoint.create("produced", factory))
            .filter(x -> x % 2 == 0)
            .via(Checkpoint.create("filtered", factory))
            .runForeach(System.out::println, materializer);
    }
}
