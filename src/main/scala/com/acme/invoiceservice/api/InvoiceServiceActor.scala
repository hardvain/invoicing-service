package com.acme.invoiceservice.api

import akka.actor.{Actor, ActorRefFactory}
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.services.InvoicingService
import spray.http.StatusCodes
import spray.json.DeserializationException
import spray.routing._

class InvoiceServiceActor(config: InvoiceServiceConfig, invoicingService: InvoicingService) extends Actor with HttpService {
  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(config, invoicingService, context)

  implicit val executionContext = context.system


  implicit def customExceptionHandler = ExceptionHandler {
    case e:DeserializationException =>
      ctx => ctx.complete(StatusCodes.BadRequest, e.msg)
  }

  implicit def customRejectionHandler = RejectionHandler {
    case List(r:MalformedRequestContentRejection) => ctx => ctx.complete(StatusCodes.BadRequest, r.message)
  }

  override def receive: Receive = runRoute(invoiceServiceApi.routes)

  override implicit def actorRefFactory: ActorRefFactory = context
}
