package com.example.javadsl;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.checkpoint.CheckpointBackend;
import akka.stream.checkpoint.DropwizardBackend;
import akka.stream.checkpoint.javadsl.Checkpoint;
import akka.stream.javadsl.Source;
import com.codahale.metrics.MetricRegistry;

public class DropwizardExample {

    public static void main(String[] args) {
        final ActorSystem system        = ActorSystem.create("DropwizardExample");
        final Materializer materializer = ActorMaterializer.create(system);

        // #dropwizard
        final MetricRegistry metricRegistry = new MetricRegistry();
        final CheckpointBackend backend     = DropwizardBackend.fromRegistry(metricRegistry);

        Source.range(1, 100)
            .via(Checkpoint.create("produced", backend))
            .filter(x -> x % 2 == 0)
            .via(Checkpoint.create("filtered", backend))
            .runForeach(System.out::println, materializer);
        // #dropwizard
    }
}
