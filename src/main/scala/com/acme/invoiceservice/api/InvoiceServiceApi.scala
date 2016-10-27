package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.services.InvoicingService
import spray.http.MediaTypes._
import spray.http.{HttpEntity, StatusCodes}
import spray.json._
import spray.routing._
import com.acme.invoiceservice.models.InvoiceProtocol._
import DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport._
case class InvoiceServiceApi(config: InvoiceServiceConfig, invoicingService: InvoicingService, refFactory: ActorRefFactory)
  extends HttpService {

  val routes: Route = {
    path("invoices") {
      get {
        parameterMap { params =>
          val invoices: List[Invoice] = params.size match {
            case 0 => invoicingService.getAllInvoices
            case _ => invoicingService.getInvoiceForFilters(params)
          }
          val response = invoices.length match {
            case 0 =>
              "The filter criteria yielded no results"
            case _ =>
              JsArray(invoices.map(_.toJson).toVector).toString()
          }
          complete(StatusCodes.OK, HttpEntity(`application/json`, response))
        }
      } ~
      post{
        entity(as[Invoice]) { invoice =>
          invoicingService.addInvoice(invoice)
          complete("")
        }
      }
    }
  }

  override implicit def actorRefFactory: ActorRefFactory = refFactory
}
