```$bash
sbt "project benchmarks" "jmh:run -i 20 -wi 10 -f1 -t1 eu.svez.stream.checkpoint.benchmarks.DropwizardCheckpointBenchmark"
```