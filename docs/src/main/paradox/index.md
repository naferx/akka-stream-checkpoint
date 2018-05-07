# Akka Streams Checkpoint

Checkpoint stage to monitor Akka Streams streaming applications. It provides means to create pass-through flows of type
`Flow[T, T, NotUsed]`, aka *Checkpoints*, to instrument your application with. This will give you a pulse on different
metrics at different points of your pipeline, such as throughput and backpressure.

`akka-stream-checkpoint` presently supports pluggable backends for

* [Dropwizard](http://metrics.dropwizard.io)
* [Kamon](https://kamon.io)

It can also be used with your own [custom backend](backends.md), should you want to write your own.

This library is currently available for Scala 2.11 and 2.12, and it uses:

* @extref[Akka Streams](akka-docs:scala/stream/index.html) $akkaVersion$ ([Github](https://github.com/akka/akka))
* @extref[Dropwizard](dw-docs:) $dropwizardVersion$ ([Github](https://github.com/dropwizard/metrics))
* @extref[Kamon](kamon-docs:/get-started) $kamonVersion$ ([Github](https://github.com/kamon-io/Kamon))

Akka Streams Checkpoint source code is available on [Github](https://github.com/svezfaz/akka-stream-checkpoint).

## Modules

### Core
The core module includes the DSLs that allow you to create the checkpoint stages to instrument your Akka Streams pipeline.

sbt
:   @@@vars
    ```scala
    libraryDependencies += "com.github.svezfaz" %% "akka-stream-checkpoint-core" % "$version$"
    ```
    @@@

Maven
:   @@@vars
    ```xml
    <dependency>
      <groupId>com.github.svezfaz</groupId>
      <artifactId>akka-stream-checkpoint-core_$scalaBinaryVersion$</artifactId>
      <version>$version$</version>
    </dependency>
    ```
    @@@

Gradle
:   @@@ vars
    ```
    dependencies {
      compile group: "com.github.svezfaz", name: "akka-stream-checkpoint-core_$scalaBinaryVersion$", version: "$version$"
    }
    ```
    @@@

### Dropwizard backend
This module provides support to back your checkpoints with [Dropwizard](http://metrics.dropwizard.io) metrics.

sbt
:   @@@vars
    ```scala
    libraryDependencies += "com.github.svezfaz" %% "akka-stream-checkpoint-dropwizard" % "$version$"
    ```
    @@@

Maven
:   @@@vars
    ```xml
    <dependency>
      <groupId>com.github.svezfaz</groupId>
      <artifactId>akka-stream-checkpoint-dropwizard_$scalaBinaryVersion$</artifactId>
      <version>$version$</version>
    </dependency>
    ```
    @@@

Gradle
:   @@@ vars
    ```
    dependencies {
      compile group: "com.github.svezfaz", name: "akka-stream-checkpoint-dropwizard_$scalaBinaryVersion$", version: "$version$"
    }
    ```
    @@@
    
### Kamon backend
This module provides support to back your checkpoints with [Kamon](https://kamon.io) metrics.

sbt
:   @@@vars
    ```scala
    libraryDependencies += "com.github.svezfaz" %% "akka-stream-checkpoint-kamon" % "$version$"
    ```
    @@@

Maven
:   @@@vars
    ```xml
    <dependency>
      <groupId>com.github.svezfaz</groupId>
      <artifactId>akka-stream-checkpoint-kamon_$scalaBinaryVersion$</artifactId>
      <version>$version$</version>
    </dependency>
    ```
    @@@

Gradle
:   @@@ vars
    ```
    dependencies {
      compile group: "com.github.svezfaz", name: "akka-stream-checkpoint-kamon_$scalaBinaryVersion$", version: "$version$"
    }
    ```
    @@@
    
## scaladsl and javadsl

There are two separate packages named `akka.stream.checkpoint.scaladsl` and `akka.stream.checkpoint.javadsl` 
with the API for Scala and Java. These packages contain a `Checkpoint` class with factory methods to create a 
checkpoint given a name (label) and a backend. See the [examples](examples.md) section for more information.

@@ toc { .main depth=3 }

@@@ index

* [Motivation](motivation.md)
* [Backends](backends.md)
* [Examples](examples.md)
* [Demo](demo.md)

@@@