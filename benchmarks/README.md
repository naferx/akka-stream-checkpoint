# Benchmarks

They can be run with

```$bash
sbt "benchmarks/jmh:run -i 20 -wi 10 -f1 -t1 akka.stream.checkpoint.benchmarks.CheckpointBenchmark"
```

To see all available options run

```$bash
sbt "benchmarks/jmh:run -h"
```