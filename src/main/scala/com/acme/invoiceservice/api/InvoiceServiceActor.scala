package com.acme.invoiceservice.api

import akka.actor.{Actor, ActorRefFactory}
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.api.InvoiceServiceApi._
import com.acme.invoiceservice.services.InvoicingService
import spray.routing._

/**
  * The handler that is provided to Spray to handle incoming requests
  * @param config
  * @param invoicingService
  */
class InvoiceServiceActor(config: InvoiceServiceConfig, invoicingService: InvoicingService) extends Actor with HttpService {
  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(config, invoicingService, context)

  implicit val executionContext = context.system

  override def receive: Receive = runRoute(invoiceServiceApi.routes)

  override implicit def actorRefFactory: ActorRefFactory = context
}
