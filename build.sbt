organization in ThisBuild := "de.kandddinsky"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test


lazy val `kandddinsky-cqrs-workshop` = (project in file("."))
  .aggregate(`airbnc-api`, `airbnc-impl`)

lazy val `airbnc-api` = (project in file("airbnc-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
lazy val `airbnc-impl` = (project in file("airbnc-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`airbnc-api`)

