package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.InvoiceServiceConfig
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.repository.Repository
import com.acme.invoiceservice.services.InvoicingService
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import spray.http.{HttpEntity, MediaTypes, StatusCodes}
import spray.json.DeserializationException
import spray.routing._
import spray.testkit.ScalatestRouteTest
import com.acme.invoiceservice.api.InvoiceServiceApi._
import com.acme.invoiceservice.exceptions.ApplicationException

class InvoiceServiceApiSpec extends FreeSpec with ScalatestRouteTest with Matchers with MockFactory {

  class InvoicingServiceStub extends InvoicingService(mock[Repository[Invoice]])

  private val mockInvoicingService: InvoicingService = mock[InvoicingServiceStub]
  val context = mock[ActorRefFactory]
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(ConfigFactory.load())
  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(invoiceServiceConfig, mockInvoicingService, context)
  val dateTime: DateTime = DateTime.now()
  "Invoices" - {
    "GET" - {
      "should return 200 OK when a GET request is made " in {
        val invoices = List(
          Invoice("1", "customer1", "address1", 1, "regular", "invoice", dateTime, dateTime, 1, dateTime, dateTime, "description", 10, 10, 10),
          Invoice("1", "customer2", "address2", 2, "regular", "invoice", dateTime, dateTime, 2, dateTime, dateTime, "description", 10, 10, 10),
          Invoice("1", "customer3", "address3", 3, "regular", "invoice", dateTime, dateTime, 3, dateTime, dateTime, "description", 10, 10, 10)
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
          responseAs[String] should be("[]")
        }
      }

      "should return appropriate error message on Application Exception" in {
        val exceptionMessage: String = "Oops, something went wrong while getting invoices"
        (mockInvoicingService.getAllInvoices _).expects().throws(ApplicationException(exceptionMessage))
        Get("/invoices") ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.InternalServerError)
          responseAs[String] should be(exceptionMessage)
        }
      }
    }
    "POST" - {
      val invoiceId: String = "invoice1"
      val customerId: String = "customer1"
      val address: String = "random address"
      val month: String = """3"""
      val invoiceType: String = "regular"
      val invoiceLocalized: String = "locale specific invoice"
      val date: String = s"${dateTime.toString()}"
      val invoiceNumber: String = """1"""
      val description: String = "description"
      val amount: String = """100"""
      val vatAmount: String = """200"""
      val totalAmount: String = """300"""
      "should create invoice when posted with valid data" in {
        val dataJsonString =
          s"""
          { "invoiceId":"$invoiceId", "customerId":"$customerId", "address":"$address", "month":$month,
            "invoiceType":"$invoiceType", "invoiceTypeLocalized":"$invoiceLocalized", "invoiceDate":"${dateTime.toString}",
            "paymentDueDate":"${dateTime.toString}", "invoiceNumber":$invoiceNumber, "startDate":"${dateTime.toString}", "endDate":"${dateTime.toString}",
            "periodDescription":"$description", "amount":$amount, "vatAmount":$vatAmount, "totalAmount":$totalAmount
          }
        """.stripMargin
        val invoice = Invoice(invoiceId, customerId, address, month.toInt, invoiceType, invoiceLocalized, dateTime, dateTime,
          invoiceNumber.toInt, dateTime, dateTime, description, amount.toDouble, vatAmount.toDouble, totalAmount.toDouble)
        (mockInvoicingService.addInvoice _).expects(*)
        Post("/invoices", HttpEntity(MediaTypes.`application/json`, dataJsonString)) ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.OK)
        }
      }

      "should throw bad request when one of the mandatory fields are missing" in {
        val dataJsonString =
          s"""
          { "invoiceId":"$invoiceId",  "address":"$address", "month":$month,
            "invoiceType":"$invoiceType", "invoiceTypeLocalized":"$invoiceLocalized", "invoiceDate":"$date",
            "paymentDueDate":"$date", "invoiceNumber":$invoiceNumber, "startDate":"$date", "endDate":"$date",
            "periodDescription":"$description", "amount":$amount, "vatAmount":$vatAmount, "totalAmount":$totalAmount
          }
        """.stripMargin
        Post("/invoices", HttpEntity(MediaTypes.`application/json`, dataJsonString)) ~> invoiceServiceApi.sealRoute(invoiceServiceApi.routes) ~> check {
          status should be(StatusCodes.BadRequest)
        }
      }

      "should return appropriate error message on Application Exception" in {
        val dataJsonString =
          s"""
          { "invoiceId":"$invoiceId", "customerId":"$customerId", "address":"$address", "month":$month,
            "invoiceType":"$invoiceType", "invoiceTypeLocalized":"$invoiceLocalized", "invoiceDate":"${dateTime.toString}",
            "paymentDueDate":"${dateTime.toString}", "invoiceNumber":$invoiceNumber, "startDate":"${dateTime.toString}", "endDate":"${dateTime.toString}",
            "periodDescription":"$description", "amount":$amount, "vatAmount":$vatAmount, "totalAmount":$totalAmount
          }
        """.stripMargin
        val invoice = Invoice(invoiceId, customerId, address, month.toInt, invoiceType, invoiceLocalized, dateTime, dateTime,
          invoiceNumber.toInt, dateTime, dateTime, description, amount.toDouble, vatAmount.toDouble, totalAmount.toDouble)
        val exceptionMessage: String = "Oops, something went wrong adding the invocie"
        (mockInvoicingService.addInvoice _).expects(*).throws(ApplicationException(exceptionMessage))
        Post("/invoices", HttpEntity(MediaTypes.`application/json`, dataJsonString)) ~> invoiceServiceApi.routes ~> check {
          status should be(StatusCodes.InternalServerError)
          responseAs[String] should be(exceptionMessage)
        }
      }
    }
  }
}
