```$bash
sbt "project benchmarks" "jmh:run -i 20 -wi 10 -f1 -t1 akka.stream.checkpoint.benchmarks.DropwizardCheckpointBenchmark"
```