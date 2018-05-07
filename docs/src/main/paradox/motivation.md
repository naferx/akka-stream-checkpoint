# Motivation

[Reactive Streams](http://www.reactive-streams.org/) are the key to build asynchronous, data-intensive applications with 
no predetermined data volumes. By enabling non-blocking backpressure, they boost the resiliency of your systems by design.
[Akka Streams](akka-docs:scala/stream/index.html) is a widely used toolkit that internally implements the 
Reactive Streams JVM specification, whilst offering a much more user-friendly API.

But how do you tune and debug such applications? When productionising Reactive Streams, the same backpressure that 
preserves the safety of your pipeline can get in the way of effectively monitoring its status. Akka Streams doesn't 
currently provide a comprehensive solution to this issue. 

TODO

## What this library does

### Throughput
TODO

### Backpressure
TODO

### Liveness
TODO

## What this library does NOT

### Error tracking
TODO this is already offered by Flow.monitor

### Trace context
TODO this can be implemented at a different level. it does not fit non-linear streaming processes well