package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.repository.Repository
import com.acme.invoiceservice.services.InvoicingService
import com.typesafe.config.{Config, ConfigFactory}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import spray.http.HttpHeaders.`Content-Type`
import spray.http.MediaTypes._
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class InvoiceServiceApiSpec extends FreeSpec with ScalatestRouteTest with Matchers with MockFactory {

  class InvoicingServiceStub extends InvoicingService(mock[Repository[Invoice]])

  private val mockInvoicingService: InvoicingService = mock[InvoicingServiceStub]
  val context = mock[ActorRefFactory]
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(ConfigFactory.load())
  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(invoiceServiceConfig, mockInvoicingService, context)
  "Invoices" - {
    "GET" - {
      "should return 200 OK when a GET request is made " in {
        val invoices = List(
          Invoice("1", "customer1", "address1", 1, "regular", "invoice", "date", "date", 1, "date", "date", "description", 10, 10, 10),
          Invoice("1", "customer2", "address2", 2, "regular", "invoice", "date", "date", 2, "date", "date", "description", 10, 10, 10),
          Invoice("1", "customer3", "address3", 3, "regular", "invoice", "date", "date", 3, "date", "date", "description", 10, 10, 10)
        )
        val parameterMap = Map("customerId" -> "customer1", "addressId" -> "address1")
        (mockInvoicingService.getInvoiceForFilters _).expects(parameterMap).returns(invoices)
        Get("/invoices?customerId=customer1&addressId=address1") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
        }
      }

      "should return appropriate message when search results are empty " in {
        val parameterMap = Map("customerId" -> "customer1", "addressId" -> "address1")
        (mockInvoicingService.getInvoiceForFilters _).expects(parameterMap).returns(List[Invoice]())
        Get("/invoices?customerId=customer1&addressId=address1") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          responseAs[String] should be("The filter criteria yielded no results")
        }
      }

      "should call getAllInvoices when no filter params are passed " in {
        (mockInvoicingService.getAllInvoices _).expects().returns(List())
        Get("/invoices") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
          responseAs[String] should be("The filter criteria yielded no results")
        }
      }
    }
    "POST" - {
      "should create invoice when posted with valid data" in {
        val invoiceId: String = """"invoice1""""
        val customerId: String = """"customer1""""
        val address: String = """"random address""""
        val month: String = """3"""
        val invoiceType: String = """"regular""""
        val invoiceLocalized: String = """"locale specific invoice""""
        val date: String = """"3/3/89""""
        val invoiceNumber: String = """1"""
        val description: String = """"description""""
        val amount: String = """100"""
        val vatAmount: String = """200"""
        val totalAmount: String = """300"""
        val data =
          s"""
          {
            "invoiceId":$invoiceId,
            "customerId":$customerId,
            "address":$address,
            "month":$month,
            "invoiceType":$invoiceType,
            "invoiceTypeLocalized":$invoiceLocalized
            "invoiceDate":$date,
            "paymentDueDate":$date,
            "invoiceNumber":$invoiceNumber,
            "startDate":$date,
            "periodDescription":$description,
            "amount":$amount,
            "vatAmount":$vatAmount,
            "totalAmount":$totalAmount"
          }
        """.stripMargin
        val invoice = Invoice(invoiceId, customerId, address, month.toInt, invoiceType, invoiceLocalized, date, date,
          invoiceNumber.toInt, date, date, description, amount.toDouble, vatAmount.toDouble, totalAmount.toDouble)
        (mockInvoicingService.addInvoice _).expects(invoice)
        Post("/invoices", data) ~> addHeader(`Content-Type`(`application/json`)) ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
        }
      }
    }
  }
}
