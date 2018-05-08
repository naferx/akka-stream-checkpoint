# Getting started

## Dependencies

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
checkpoint given a name (label) and a backend.

## Examples

### Dropwizard

These examples require `akka-stream-checkpoint-core` and `akka-stream-checkpoint-dropwizard` modules.
 
Scala
: @@ snip [dummy](../../test/scala/com/example/scaladsl/DropwizardExample.scala) { #dropwizard }

Java
: @@ snip [dummy](../../test/java/com/example/javadsl/DropwizardExample.java) { #dropwizard }

### Kamon
 
These examples require `akka-stream-checkpoint-core` and `akka-stream-checkpoint-kamon` modules.
 
Scala
: @@ snip [dummy](../../test/scala/com/example/scaladsl/KamonExample.scala) { #kamon }

Java
: @@ snip [dummy](../../test/java/com/example/javadsl/KamonExample.java) { #kamon }

### Sugared DSL

Regardless of the chosen backend, some syntactic sugar is available for Scala users under `scaladsl.Implicits`.

Scala
: @@ snip [dummy](../../test/scala/com/example/scaladsl/PimpedExample.scala) { #pimped }
