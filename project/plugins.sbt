scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

addSbtPlugin("com.geirsson"     % "sbt-scalafmt" % "1.5.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl"    % "1.3.13")
addSbtPlugin("com.eed3si9n"     % "sbt-assembly" % "0.14.5")
addSbtPlugin("org.scalatra.sbt" % "sbt-scalatra" % "1.0.1")
addSbtPlugin("com.jsuereth"     % "sbt-pgp"      % "1.1.0")
//addSbtPlugin("io.spray" % "sbt-twirl" % "0.7.0")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtCoursier
addSbtPlugin("com.typesafe.sbt" % "sbt-license-report" % "1.2.0")

//addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
