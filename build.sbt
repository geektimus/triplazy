lazy val commonSettings = Seq(
  organization := "com.codingmaniacs.triplazy",
  scalaVersion := "2.12.8",
  version := "0.1.0-SNAPSHOT"
)

lazy val root = (project in file("."))
  .enablePlugins(UniversalPlugin, DockerPlugin)
  .settings(
    commonSettings,
    name := "TripLazy"
  )
  .aggregate(carRental, hotelBooking, flightBooking)

lazy val carRental = (project in file("car-rental-service"))
lazy val hotelBooking = (project in file("hotel-booking-service"))
lazy val flightBooking = (project in file("flight-booking-service"))
