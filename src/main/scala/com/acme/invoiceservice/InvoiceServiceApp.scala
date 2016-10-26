package com.acme.invoiceservice

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import com.acme.invoiceservice.api.InvoiceServiceApi
import com.typesafe.config.ConfigFactory
import spray.can.Http

object InvoiceServiceApp extends App {
  implicit val system = ActorSystem("com-acme-invoiceservice")
  val config = ConfigFactory.load()
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(config)
  private val invoiceApiActor: ActorRef = system.actorOf(Props(classOf[InvoiceServiceApi], invoiceServiceConfig), "invoice-api")
  IO(Http) ! Http.Bind(invoiceApiActor, invoiceServiceConfig.host, invoiceServiceConfig.port)
}




