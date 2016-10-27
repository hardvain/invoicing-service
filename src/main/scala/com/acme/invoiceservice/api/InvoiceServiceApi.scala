package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.exceptions.ApplicationException
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.models.InvoiceProtocol._
import com.acme.invoiceservice.services.InvoicingService
import spray.http.MediaTypes._
import spray.http.{HttpEntity, StatusCodes}
import spray.httpx.SprayJsonSupport._
import spray.json._
import spray.routing._

case class InvoiceServiceApi(config: InvoiceServiceConfig, invoicingService: InvoicingService, refFactory: ActorRefFactory)
  extends HttpService {

  val routes: Route = {
    path("invoices") {
      get {
        parameterMap { params =>
          try {
            val httpEntity = params.isEmpty match {
              case true => getAllInvoices
              case false => getInvoiceForFilters(params)
            }
            complete(StatusCodes.OK, httpEntity)
          } catch {
            case ApplicationException(message) => complete(StatusCodes.InternalServerError, message)
          }
        }
      } ~
        post {
          entity(as[Invoice]) { invoice =>
            try {
              invoicingService.addInvoice(invoice)
              complete(StatusCodes.OK)
            } catch {
              case ApplicationException(message) => complete(StatusCodes.InternalServerError, message)
            }
          }
        }
    }
  }

  override implicit def actorRefFactory: ActorRefFactory = refFactory

  private def getInvoiceForFilters(filterMap: Map[String, String]): HttpEntity = {
    val invoices: List[Invoice] = invoicingService.getInvoiceForFilters(filterMap)
    invoices.isEmpty match {
      case true => HttpEntity(`application/json`, "The filter criteria yielded no results")
      case false => HttpEntity(`application/json`, JsArray(invoices.map(_.toJson).toVector).toString())
    }
  }

  private def getAllInvoices: HttpEntity = {
    val invoices: List[Invoice] = invoicingService.getAllInvoices
    HttpEntity(`application/json`, JsArray(invoices.map(_.toJson).toVector).toString())
  }
}

object InvoiceServiceApi {
  implicit def customExceptionHandler = ExceptionHandler {
    case e: DeserializationException =>
      ctx => ctx.complete(StatusCodes.BadRequest, e.msg)
  }

  implicit def customRejectionHandler = RejectionHandler {
    case List(r: MalformedRequestContentRejection) => ctx => ctx.complete(StatusCodes.BadRequest, r.message)
  }
}