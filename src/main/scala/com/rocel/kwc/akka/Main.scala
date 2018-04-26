package com.rocel.kwc.akka


import java.awt.Desktop

import org.apache.commons.cli.{DefaultParser, HelpFormatter, _}

import scala.language.postfixOps

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val header = """
                   | _          __ _                           _
                   || | ____ _ / _| | ____ _     __      _____| |__         ___ ___  _ __  ___ _   _ _ __ ___   ___ _ __
                   || |/ / _` | |_| |/ / _` |____\ \ /\ / / _ \ '_ \ _____ / __/ _ \| '_ \/ __| | | | '_ ` _ \ / _ \ '__|
                   ||   < (_| |  _|   < (_| |_____\ V  V /  __/ |_) |_____| (_| (_) | | | \__ \ |_| | | | | | |  __/ |
                   ||_|\_\__,_|_| |_|\_\__,_|      \_/\_/ \___|_.__/       \___\___/|_| |_|___/\__,_|_| |_| |_|\___|_|
                   |                                                                                                     
                   |
                   |""".stripMargin

    val options = new Options()

    val helpArg = new Option("h", "help", false, "Prints this message.")
    options.addOption(helpArg)

    val bootstrapServerArg = new Option("b", "bootstrap-server", true, "REQUIRED: The server to connect to.")
    //    bootstrapServerArg.setRequired(true)
    options.addOption(bootstrapServerArg)

    val topicArg = new Option("t", "topic", true, "REQUIRED: The topic id to consume on.")
    //    topicArg.setRequired(true)
    options.addOption(topicArg)

    val componentArg = new Option("c", "component", true, "REQUIRED: Client component viewer [cards, list, map, pivot, table].")
    //    componentArg.setRequired(true)
    options.addOption(componentArg)

    val portArg = new Option("p", "port", true, "Port on which will start Kafka-Web-Consumer")
    portArg.setRequired(false)
    options.addOption(portArg)

    val bufferSizeArg = new Option("bs", "buffer-size", true, "Size of the buffer on the UI.")
    bufferSizeArg.setRequired(false)
    options.addOption(bufferSizeArg)

    val fromBeginning = new Option("fb", "from-beginning", false, "If the consumer does not already have an established offset to consume from, start with the earliest message present in the log rather than the latest message")
    fromBeginning.setRequired(false)
    options.addOption(fromBeginning)

    val parser = new DefaultParser
    val formatter = new HelpFormatter

    try {
      val cmd = parser.parse(options, args)

      if (cmd.hasOption("help")) {
        println(header)
        formatter.printHelp("kafka-web-consumer", options)
        return
      }

      val bootstrapServer = cmd.getOptionValue("bootstrap-server")
      val topic = cmd.getOptionValue("topic")
      val component = cmd.getOptionValue("component", "table")
      val port = TypeHandler.createNumber(cmd.getOptionValue("port", "9000")).intValue()
      val bufferSize = TypeHandler.createNumber(cmd.getOptionValue("buffer-size", "500")).intValue()
      val fromBeginning = cmd.getOptionValue("from-beginning", "false").toBoolean

      println(header)

      val uri = Engine.run(bootstrapServer, topic, fromBeginning, port, component, bufferSize)

      if (Desktop.isDesktopSupported) {
        Desktop.getDesktop.browse(uri)
      }

      println(s"Open $uri")

    } catch {
      case e: Exception =>
        System.err.println(e.getMessage)
        System.exit(1)
    }
  }

}