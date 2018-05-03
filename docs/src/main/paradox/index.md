# Akka Streams Checkpoint

Checkpoint stage to monitor Akka Streams streaming applications.

The examples in this documentation use

* Scala $scalaBinaryVersion$
* @extref[Akka Streams](akka-docs:scala/stream/index.html) $akkaVersion$ ([Github](https://github.com/akka/akka))
* @extref[Dropwizard](dw-docs:) $dropwizardVersion$ ([Github](https://github.com/dropwizard/metrics))
* @extref[Kamon](kamon-docs:) $kamonVersion$ ([Github](https://github.com/kamon-io/Kamon))


## Dependencies

sbt
:   @@@vars
    ```scala
    libraryDependencies += "com.github.svezfaz" %% "akka-stream-checkpoint" % "$version$"
    ```
    @@@

Maven
:   @@@vars
    ```xml
    <dependency>
      <groupId>com.github.svezfaz</groupId>
      <artifactId>akka-stream-checkpoint_$scalaBinaryVersion$</artifactId>
      <version>$version$</version>
    </dependency>
    ```
    @@@

Gradle
:   @@@ vars
    ```
    dependencies {
      compile group: "com.github.svezfaz", name: "akka-stream-checkpoint_$scalaBinaryVersion$", version: "$version$"
    }
    ```
    @@@

