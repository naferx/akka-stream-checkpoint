# Akka Streams Checkpoint

Checkpoint stage to monitor Akka Streams streaming applications. Presently supports pluggable backends for
- [Dropwizard](http://metrics.dropwizard.io)
- [Kamon](https://kamon.io)

The examples in this documentation use

* Scala $scalaBinaryVersion$
* @extref[Akka Streams](akka-docs:scala/stream/index.html) $akkaVersion$ ([Github](https://github.com/akka/akka))
* @extref[Dropwizard](dw-docs:) $dropwizardVersion$ ([Github](https://github.com/dropwizard/metrics))
* @extref[Kamon](kamon-docs:) $kamonVersion$ ([Github](https://github.com/kamon-io/Kamon))


## Dependencies

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

