# Akka Streams Checkpoint

Checkpoint stage to monitor Akka Streams streaming applications. Presently supports pluggable backends for
- [Dropwizard](http://metrics.dropwizard.io)
- [Kamon](https://kamon.io)

This library is currently available for Scala 2.11 and 2.12.

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

### Dropwizard
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
    
### Kamon
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
    
    
## Examples

The examples in this documentation use

* Scala $scalaBinaryVersion$
* @extref[Akka Streams](akka-docs:scala/stream/index.html) $akkaVersion$ ([Github](https://github.com/akka/akka))
* @extref[Dropwizard](dw-docs:) $dropwizardVersion$ ([Github](https://github.com/dropwizard/metrics))
* @extref[Kamon](kamon-docs:) $kamonVersion$ ([Github](https://github.com/kamon-io/Kamon))