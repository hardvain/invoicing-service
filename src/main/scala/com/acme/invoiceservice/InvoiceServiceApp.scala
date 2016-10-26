package com.acme.invoiceservice

import java.io.InputStreamReader

import spray.routing.RejectionHandler.Default
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, ActorRefFactory, ActorSystem, Props}
import akka.io.IO
import com.acme.invoiceservice.api.InvoiceServiceApi
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigSyntax}
import spray.can.Http
import spray.routing.{HttpService, Route}

object InvoiceServiceApp extends App {
  implicit val system = ActorSystem("com-acme-invoiceservice")
  val config = ConfigFactory.load()
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(config)
  private val invoiceApiActor: ActorRef = system.actorOf(Props(classOf[InvoiceServiceApi], invoiceServiceConfig), "invoice-api")
  IO(Http) ! Http.Bind(invoiceApiActor, invoiceServiceConfig.host, invoiceServiceConfig.port)
}




