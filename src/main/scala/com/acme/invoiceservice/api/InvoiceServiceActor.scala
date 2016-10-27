package com.acme.invoiceservice.api

import akka.actor.{Actor, ActorRefFactory}
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.services.InvoicingService
import spray.http.StatusCodes
import spray.json.DeserializationException
import spray.routing.{HttpService,RejectionHandler,ExceptionHandler,Route,RequestContext,RoutingSettings}
import spray.util.LoggingContext

class InvoiceServiceActor(config: InvoiceServiceConfig, invoicingService: InvoicingService) extends Actor with HttpService {
  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(config, invoicingService, context)

  implicit val executionContext = context.system


  implicit def exceptionHandler = ExceptionHandler {
    case e:DeserializationException =>
      ctx => ctx.complete(StatusCodes.BadRequest, e.msg)
  }

  override def receive: Receive = runRoute(invoiceServiceApi.routes)

  override implicit def actorRefFactory: ActorRefFactory = context
}
