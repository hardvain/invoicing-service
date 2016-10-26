package com.acme.invoiceservice.api

import akka.actor.{Actor, ActorRefFactory}
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.services.InvoicingService
import spray.http.MediaTypes._
import spray.http.{HttpEntity, StatusCodes}
import spray.json.JsArray
import spray.routing._
import spray.json._
import spray.json.DefaultJsonProtocol._
import com.acme.invoiceservice.models.InvoiceProtocol._
case class InvoiceServiceApi(config: InvoiceServiceConfig, invoicingService: InvoicingService) extends Actor with HttpService {
  override implicit def actorRefFactory: ActorRefFactory = context

  implicit val system = context.system

  override def receive: Receive = runRoute(invoiceRoutes)

  val invoiceRoutes: Route = {
    path("invoices") {
      get {
        parameterMap { params =>
          val invoices: List[Invoice] = invoicingService.getInvoiceForFilters(params)
          val invoiceJsValues: Vector[JsValue] = invoices.map(_.toJson).toVector
          complete(StatusCodes.OK, HttpEntity(`application/json`, JsArray(invoiceJsValues).toString()))
        }
      }
    }
  }
}
