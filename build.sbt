organization in ThisBuild := "io.sudostream"
version in ThisBuild := "0.0.1"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `timetoteach` = (project in file("."))
  .aggregate(`timetoteach-api`, `timetoteach-impl`, `timetoteach-stream-api`, `timetoteach-stream-impl`)

lazy val `timetoteach-api` = (project in file("timetoteach-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `timetoteach-impl` = (project in file("timetoteach-impl"))
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
  .dependsOn(`timetoteach-api`)

lazy val `timetoteach-stream-api` = (project in file("timetoteach-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `timetoteach-stream-impl` = (project in file("timetoteach-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`timetoteach-stream-api`, `timetoteach-api`)
