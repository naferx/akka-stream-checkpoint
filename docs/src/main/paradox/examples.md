# Examples

##Dropwizard

These examples require `akka-stream-checkpoint-core` and `akka-stream-checkpoint-dropwizard` modules.
 
Scala
: @@ snip [dummy](../../test/scala/com/example/scaladsl/DropwizardExample.scala) { #dropwizard }

Java
: @@ snip [dummy](../../test/java/com/example/javadsl/DropwizardExample.java) { #dropwizard }

##Kamon
 
These examples require `akka-stream-checkpoint-core` and `akka-stream-checkpoint-kamon` modules.
 
Scala
: @@ snip [dummy](../../test/scala/com/example/scaladsl/KamonExample.scala) { #kamon }

Java
: @@ snip [dummy](../../test/java/com/example/javadsl/KamonExample.java) { #kamon }

##Sugared DSL

Regardless of the chosen backend, some syntactic sugar is available for Scala users under `scaladsl.Implicits`.

Scala
: @@ snip [dummy](../../test/scala/com/example/scaladsl/PimpedExample.scala) { #pimped }
