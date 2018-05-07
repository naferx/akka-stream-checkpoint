// benchmarks
addSbtPlugin("pl.project13.scala"    % "sbt-jmh"      % "0.3.3")

// publishing
addSbtPlugin("com.jsuereth"          % "sbt-pgp"      % "1.1.0")
addSbtPlugin("org.xerial.sbt"        % "sbt-sonatype" % "2.1")

// site
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox"  % "0.3.3")
addSbtPlugin("com.typesafe.sbt"      % "sbt-site"     % "1.3.2" exclude("com.lightbend.paradox", "sbt-paradox"))
addSbtPlugin("com.typesafe.sbt"      % "sbt-ghpages"  % "0.6.2")