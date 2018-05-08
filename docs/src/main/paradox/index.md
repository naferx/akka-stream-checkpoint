# Akka Streams Checkpoint

Checkpoint stage to monitor Akka Streams streaming applications. It provides means to create pass-through flows of type
`Flow[T, T, NotUsed]`, aka *Checkpoints*, to instrument your application with. This will give you a pulse on different
metrics at different points of your pipeline, such as throughput and backpressure.

`akka-stream-checkpoint` presently supports pluggable backends for

* [Dropwizard](http://metrics.dropwizard.io)
* [Kamon](https://kamon.io)

It can also be used with your own @ref:[custom backend](backends.md), should you want to write your own.

This library is currently available for Scala 2.11 and 2.12, and it uses:

* @extref[Akka Streams](akka-docs:scala/stream/index.html) $akkaVersion$ ([Github](https://github.com/akka/akka))
* @extref[Dropwizard](dw-docs:) $dropwizardVersion$ ([Github](https://github.com/dropwizard/metrics))
* @extref[Kamon](kamon-docs:/get-started) $kamonVersion$ ([Github](https://github.com/kamon-io/Kamon))

Akka Streams Checkpoint source code is available on [Github](https://github.com/svezfaz/akka-stream-checkpoint).

@@ toc { .main depth=3 }

@@@ index

* [Introduction](introduction.md)
* [Getting Started](gettingStarted.md)
* [Backends](backends.md)
* [Demo](demo.md)

@@@