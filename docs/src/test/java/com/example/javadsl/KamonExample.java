package com.example.javadsl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.checkpoint.CheckpointBackend;
import akka.stream.checkpoint.KamonBackend;
import akka.stream.checkpoint.javadsl.Checkpoint;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletionStage;

public class KamonExample {

    public static void main(String[] args) {
        final ActorSystem system        = ActorSystem.create("KamonExample");
        final Materializer materializer = ActorMaterializer.create(system);

        // #kamon
        final CheckpointBackend backend = KamonBackend.instance();

        final CompletionStage<Done> done = Source.range(1, 100)
                .via(Checkpoint.create("produced", backend))
                .filter(x -> x % 2 == 0)
                .via(Checkpoint.create("filtered", backend))
                .runForeach(System.out::println, materializer);
        // #kamon

        done.thenApply(d -> system.terminate());
    }
}
