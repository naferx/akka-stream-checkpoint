package com.example.javadsl;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.checkpoint.CheckpointBackend;
import akka.stream.checkpoint.CheckpointRepository;
import akka.stream.checkpoint.javadsl.Checkpoint;
import akka.stream.javadsl.Source;

public class CustomBackendExample {

    public static void main(String[] args) {
        final ActorSystem system        = ActorSystem.create("DropwizardExample");
        final Materializer materializer = ActorMaterializer.create(system);

        // #custom
        final CheckpointBackend backend = new CheckpointBackend() {
            @Override
            public CheckpointRepository createRepository(String name) {
                return new CheckpointRepository() {
                    @Override
                    public void markPush(long latencyNanos, long backpressureRatio) {
                        System.out.println(String.format("PUSH - %s: latency:%d, backpressure ratio: %d",
                                name, latencyNanos, backpressureRatio));
                    }

                    @Override
                    public void markPull(long latencyNanos) {
                        System.out.println(String.format("PULL - %s: latency:%d",
                                name, latencyNanos));
                    }
                };
            }
        };

        Source.range(1, 100)
            .via(Checkpoint.create("produced", backend))
            .filter(x -> x % 2 == 0)
            .via(Checkpoint.create("filtered", backend))
            .runForeach(System.out::println, materializer);
        // #custom
    }
}
