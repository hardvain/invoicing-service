package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.models.Invoice.InvoiceProtocol._
import com.acme.invoiceservice.models.{InMemoryInvoiceFilter, Invoice, PurchaseType}
import com.acme.invoiceservice.repository.InMemoryRepository
import com.acme.invoiceservice.services.InvoicingService
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import spray.http.{HttpEntity, MediaTypes, StatusCodes}
import spray.json._
import spray.testkit.ScalatestRouteTest

/**
  * This test sets up few invoice data ahead and tests all the requirements mentioned in the problem
  */
class RequirementsSpec extends FreeSpec with ScalatestRouteTest with Matchers with MockFactory {
  private val invoicingService: InvoicingService = InvoicingService(new InMemoryRepository[Invoice](new InMemoryInvoiceFilter))
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(ConfigFactory.load())
  private val context = mock[ActorRefFactory]
  val dateTime: DateTime = DateTime.now()

  private val invoices = List(
    Invoice("1", "customer1", "address1", 1, PurchaseType.Shop, "invoice", dateTime, dateTime, 1, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("2", "customer2", "address2", 2, PurchaseType.Regular, "invoice", dateTime, dateTime, 2, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("3", "customer3", "address3", 3, PurchaseType.Shop, "invoice", dateTime, dateTime, 3, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("4", "customer4", "address4", 4, PurchaseType.Regular, "invoice", dateTime, dateTime, 4, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("5", "customer5", "address5", 5, PurchaseType.Regular, "invoice", dateTime, dateTime, 5, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("6", "customer6", "address6", 6, PurchaseType.Regular, "invoice", dateTime, dateTime, 6, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("7", "customer7", "address7", 7, PurchaseType.Regular, "invoice", dateTime, dateTime, 7, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("8", "customer8", "address8", 8, PurchaseType.Regular, "invoice", dateTime, dateTime, 8, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("9", "customer9", "address9", 9, PurchaseType.Regular, "invoice", dateTime, dateTime, 9, dateTime, dateTime, "description", 10, 10, 10),
    Invoice("10", "customer1", "address10", 10, PurchaseType.Regular, "invoice", dateTime, dateTime, 10, dateTime, dateTime, "description", 10, 10, 10)
  )
  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(invoiceServiceConfig, invoicingService, context)

  invoices.foreach { invoice =>
    Post("/sysapi/v1.0/invoices", HttpEntity(MediaTypes.`application/json`, invoice.toJson.toString())) ~> invoiceServiceApi.routes ~> check {
      status should be(StatusCodes.OK)
    }
  }

  "Requirements" - {
    "get invoice by customer and month" - {
      "return matching value if present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1&month=1") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          val json = responseAs[String].parseJson
          assert(json.isInstanceOf[JsArray])
          assert(json.asInstanceOf[JsArray].elements.size == 1)
        }
      }
      "return no result found if not present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1&month=3") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          assert(responseAs[String] == "The filter criteria yielded no results")
        }
      }
    }

    "get invoice by customer, invoice type and month" - {
      "return matching value if present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1&month=1&purchaseType=shop") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          val json = responseAs[String].parseJson
          assert(json.isInstanceOf[JsArray])
          assert(json.asInstanceOf[JsArray].elements.size == 1)
        }
      }
      "return no result found if not present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1&month=1&purchaseType=regular") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          assert(responseAs[String] == "The filter criteria yielded no results")
        }
      }
    }

    "get invoice by customer and address" - {
      "return matching value if present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1&address=address1") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          val json = responseAs[String].parseJson
          assert(json.isInstanceOf[JsArray])
          assert(json.asInstanceOf[JsArray].elements.size == 1)
        }
      }
      "return no result found if not present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer100") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          assert(responseAs[String] == "The filter criteria yielded no results")
        }
      }
    }

    "get invoice by customer " - {
      "return matching value if present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          val json = responseAs[String].parseJson
          assert(json.isInstanceOf[JsArray])
          assert(json.asInstanceOf[JsArray].elements.size == 2)
        }
      }
      "return no result found if not present" in {
        Get("/sysapi/v1.0/invoices?customerId=customer1&address=address3") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          assert(responseAs[String] == "The filter criteria yielded no results")
        }
      }
    }
    "get all invoices " - {
      "return matching value if present" in {
        Get("/sysapi/v1.0/invoices") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          val json = responseAs[String].parseJson
          assert(json.isInstanceOf[JsArray])
          assert(json.asInstanceOf[JsArray].elements.size == 10)
        }
      }
    }
  }

}
