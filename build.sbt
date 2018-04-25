name := "kafka-web-consumer"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  "com.sparkjava" % "spark-core" % "2.7.1",

  // Kafka
  "org.apache.kafka" % "kafka-clients" % "1.0.0" exclude("org.slf4j", "slf4j-log4j12") exclude("log4j", "log4j"),

  // Akka
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.20",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "de.heikoseeberger" %% "akka-sse" % "3.0.0",

  // Config
  "com.typesafe" % "config" % "1.3.2",

  // CLI
  "commons-cli" % "commons-cli" % "1.4"

)