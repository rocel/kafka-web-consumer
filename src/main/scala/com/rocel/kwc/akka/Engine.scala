package com.rocel.kwc.akka

import java.io.File
import java.net.URI
import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives.{complete, path, _}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.duration._
import scala.language.postfixOps


object Engine {
  private val config = ConfigFactory.load().withFallback(ConfigFactory.parseFile(new File(System.getenv("HOME") + "/.kwc")))

  def run(bootstrapServer: String, topicName: String, fromBeginning: Boolean, port: Int, component: String, bufferSize: Int): URI = {
    var opts = ""
    if ("map".equals(component)) {
      if (config.hasPath("mapbox")) {
        opts += s"&mapbox_token=${config.getString("mapbox.access-token")}"
      } else {
        println("The mapbox 'access-token' is missing from the ~./kwc configuration file.")
        System.exit(1)
      }
    }

    implicit val actorSystem = ActorSystem("KWC")
    implicit val streamMaterializer = ActorMaterializer()
    implicit val executionContext = actorSystem.dispatcher
    val log = actorSystem.log

    val consumerSettings: ConsumerSettings[Array[Byte], String] = ConsumerSettings(actorSystem, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServer)
      .withGroupId(s"kwc-${UUID.randomUUID()}")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, if (fromBeginning) "earliest" else "latest")

    val routes = wsMock(consumerSettings) ~ getUI

    val bindingFuture = Http().bindAndHandle(routes, "localhost", port)
    bindingFuture
      .map(_.localAddress)
      .map(addr => s"Bound to $addr")
      .foreach(log.info)

    new java.net.URI(s"http://localhost:$port/$component?topicName=$topicName&bufferSize=$bufferSize$opts")
  }

  def getUI =
    path("") {
      getFromResource("dist/index.html")
    } ~ pathPrefix("") {
      getFromResourceDirectory("dist") ~
        getFromResource("dist/index.html")
    }

  /**
    *
    * Kafka Topic ~> HTTP Websocket Streaming Response
    * Access on http://localhost/consume?topicName=mytopic&isAvro=false
    *
    * @param consumerSettings
    */
  def wsTopic(consumerSettings: ConsumerSettings[Array[Byte], String]) =
    path("consume") {
      parameters('topicName.as[String]) {
        (topicName) => {
          println(s"topicName $topicName")
          val src = Consumer.plainSource(consumerSettings, Subscriptions.topics(topicName))
            .map(consumerRecord => TextMessage(consumerRecord.value()))

          extractUpgradeToWebSocket { upgrade =>
            complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, src))
          }
        }
      }
    }

  def wsMock(consumerSettings: ConsumerSettings[Array[Byte], String]) =
    path("consume") {
      parameters('topicName.as[String]) {
        (topicName) => {
          println(s"topicName $topicName")
          val src = Source.tick(1 seconds, 500 milliseconds, 1)
            .map(tick => TextMessage(
              s"""
                 |{
                 |"id" : "${UUID.randomUUID()}",
                 |"longitude" : "${math.random * (2.4126988 - 1.9315426) + 1.9315426}",
                 |"latitude" : "${math.random * (47.052503 - 46.753854) + 46.753854}",
                 |"some_metric" : "${math.random}",
                 |"some_metric2" : "${math.random}",
                 |"some_metric4" : "${math.random}",
                 |"some_long_dimension" : "some_dimensino_valuee",
                 |"some_long_dimension2" : "some_dimensino_value2",
                 |"some_long_dimension3" : "some_dimensino_value3"
                 |}
               """.stripMargin))

          extractUpgradeToWebSocket { upgrade =>
            complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, src))
          }
        }
      }
    }


}
