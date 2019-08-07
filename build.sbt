lazy val `grpc-scala-protocol` =
  project
    .in(file("protocol"))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning)
    .settings(name := "Protobuf (gRPC)")
    .settings(settings)
    .settings(scalaPbSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.grpcNetty,
        library.scalaPbRuntime,
        library.scalaPbRuntimeGrpc,
        library.scalaCheck % Test,
        library.scalaTest % Test
      )
    )

lazy val `flight-booking-service` =
  project
    .in(file("flight-booking-service"))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning, JavaAppPackaging, AshScriptPlugin)
    .dependsOn(`grpc-scala-protocol`)
    .settings(name := "Flight Booking Service")
    .settings(settings)
    .settings(dockerSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.cats,
        library.log4j2Api,
        library.log4j2Core,
        library.log4j2Scala,
        library.scalaLogging,
        library.slf4j,
        library.monix,
        library.pureConfig,
        library.scalaCheck % Test,
        library.scalaTest % Test
      ),
      mainClass in Compile := Some("example.Hello"),
      version in Docker := "0.1.0-SNAPSHOT",
      addCommandAlias("run-services", ";flight-booking-service/run")
    )

lazy val `hotel-booking-service` =
  project
    .in(file("hotel-booking-service"))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning, JavaAppPackaging, AshScriptPlugin)
    .dependsOn(`grpc-scala-protocol`)
    .settings(name := "Hotel Booking Service")
    .settings(settings)
    .settings(dockerSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.cats,
        library.log4j2Api,
        library.log4j2Core,
        library.log4j2Scala,
        library.scalaLogging,
        library.slf4j,
        library.monix,
        library.pureConfig,
        library.scalaCheck % Test,
        library.scalaTest % Test
      ),
      mainClass in Compile := Some("example.Hello"),
      version in Docker := "0.1.0-SNAPSHOT",
      addCommandAlias("run-services", ";hotel-booking-service/run")
    )


lazy val `car-rental-service` =
  project
    .in(file("car-rental-service"))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning, JavaAppPackaging, AshScriptPlugin)
    .dependsOn(`grpc-scala-protocol`)
    .settings(name := "Car Rental Service")
    .settings(settings)
    .settings(dockerSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.cats,
        library.log4j2Api,
        library.log4j2Core,
        library.log4j2Scala,
        library.scalaLogging,
        library.slf4j,
        library.monix,
        library.pureConfig,
        library.scalaCheck % Test,
        library.scalaTest % Test
      ),
      mainClass in Compile := Some("example.GreeterServer"),
      version in Docker := "0.1.0-SNAPSHOT",
      addCommandAlias("run-services", ";car-rental-service/run")
    )

lazy val `service-gateway` =
  project
    .in(file("gateway"))
    .enablePlugins(PlayScala, AutomateHeaderPlugin, GitVersioning, JavaAppPackaging, AshScriptPlugin)
    .dependsOn(`grpc-scala-protocol`)
    .settings(name := "Service Gateway")
    .settings(settings)
    .settings(dockerSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.cats,
        library.log4j2Api,
        library.log4j2Core,
        library.log4j2Scala,
        library.scalaLogging,
        library.slf4j,
        library.monix,
        library.pureConfig,
        library.scalaCheck % Test,
        library.scalaTest % Test
      ),
//      mainClass in Compile := Some("example.Hello"),
      version in Docker := "0.1.0-SNAPSHOT",
      addCommandAlias("run-services", ";service-gateway/run")
    )

lazy val root =
  project.in(file("."))
    .enablePlugins(UniversalPlugin, DockerPlugin)
    .settings(
      name := "TripLazy"
    )
    .aggregate(
      `grpc-scala-protocol`,
      `car-rental-service`,
      `hotel-booking-service`,
      `flight-booking-service`,
      `service-gateway`
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {

    import scalapb.compiler.Version.{grpcJavaVersion, scalapbVersion}

    object Version {
      val cats = "1.6.0"
      val log4j2 = "2.11.0"
      val log4j2Scala = "11.0"
      val scalaLogging = "3.9.2"
      val monix = "3.0.0-RC3"
      val pureconfig = "0.11.1"
      val scalaCheck = "1.14.0"
      val scalaTest = "3.0.5"
      val slf4j = "2.12.0"
    }

    val cats = "org.typelevel" %% "cats-core" % Version.cats
    val grpcNetty = "io.grpc" % "grpc-netty" % grpcJavaVersion
    val log4j2Api = "org.apache.logging.log4j" % "log4j-api" % Version.log4j2
    val log4j2Core = "org.apache.logging.log4j" % "log4j-core" % Version.log4j2 % Runtime
    val log4j2Scala = "org.apache.logging.log4j" % "log4j-api-scala_2.12" % Version.log4j2Scala
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging
    val slf4j = "org.apache.logging.log4j" % "log4j-slf4j18-impl" % Version.slf4j

    val monix = "io.monix" %% "monix" % Version.monix
    val pureConfig = "com.github.pureconfig" %% "pureconfig" % Version.pureconfig
    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val scalaPbRuntime = "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf"
    val scalaPbRuntimeGrpc = "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
    gitSettings ++
    scalafmtSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    // scalaVersion := "2.12.8",
    version := "0.1.0-SNAPSHOT",
    organization := "com.codingmaniacs.triplazy",
    headerLicense := Some(HeaderLicense.MIT("2019", "Geektimus")),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-target:jvm-1.8",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ypartial-unification",
      "-Ywarn-dead-code",
      "-Ywarn-infer-any",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value)
  )

lazy val dockerSettings =
  Seq(
    dockerBaseImage := "openjdk:8-jdk-alpine",
    dockerUpdateLatest := true
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

lazy val scalaPbSettings = Seq(
  PB.targets in Compile := Seq(
    scalapb.gen() -> (sourceManaged in Compile).value
  )
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )