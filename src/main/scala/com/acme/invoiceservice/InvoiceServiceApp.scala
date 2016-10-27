package com.acme.invoiceservice

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import com.acme.invoiceservice.api.{InvoiceServiceActor, InvoiceServiceApi}
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.repository.{InMemoryRepository, MongoRepository}
import com.acme.invoiceservice.services.InvoicingService
import com.typesafe.config.ConfigFactory
import spray.can.Http

object InvoiceServiceApp extends App {
  implicit val system = ActorSystem("com-acme-invoiceservice")
  val config = ConfigFactory.load()
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(config)
  val repository = if(invoiceServiceConfig.isInMemory){
    new InMemoryRepository[Invoice]
  } else {
    new MongoRepository[Invoice]
  }
   private val invoicingService: InvoicingService = InvoicingService(repository)
  private val invoiceServiceActor: ActorRef = system.actorOf(Props(classOf[InvoiceServiceActor], invoiceServiceConfig, invoicingService), "invoice-service-actor")
  IO(Http) ! Http.Bind(invoiceServiceActor, invoiceServiceConfig.host, invoiceServiceConfig.port)
}




