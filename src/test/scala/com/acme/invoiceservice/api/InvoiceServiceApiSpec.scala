package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.services.InvoicingService
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class InvoiceServiceApiSpec extends FreeSpec with ScalatestRouteTest with Matchers with MockFactory {
  class InvoicingServiceStub extends InvoicingService(null, null)
  private val mockInvoicingService: InvoicingService = mock[InvoicingServiceStub]
  val context = mock[ActorRefFactory]

  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(null, mockInvoicingService, context)
  "Invoices" - {
    "should return 200 OK when a GET request is made " in {
      val invoices = List(
        Invoice("1","customer1","address1",1,"regular","invoice","date","date",1,"date","date","description",10,10,10),
        Invoice("1","customer2","address2",2,"regular","invoice","date","date",2,"date","date","description",10,10,10),
        Invoice("1","customer3","address3",3,"regular","invoice","date","date",3,"date","date","description",10,10,10)
      )
      val parameterMap = Map("customerId" -> "customer1", "addressId" -> "address1")
      (mockInvoicingService.getInvoiceForFilters _).expects(parameterMap).returns(invoices)
        Get("/invoices?customerId=customer1&addressId=address1") ~> invoiceServiceApi.routes ~> check{
          status should be (StatusCodes.OK)
        }
    }

    "should return appropriate message when search results are empty " in {
      val parameterMap = Map("customerId" -> "customer1", "addressId" -> "address1")
      (mockInvoicingService.getInvoiceForFilters _).expects(parameterMap).returns(List[Invoice]())
      Get("/invoices?customerId=customer1&addressId=address1") ~> invoiceServiceApi.routes ~> check{
        status should be (StatusCodes.OK)
        responseAs[String] should be ("The filter criteria yielded no results")
      }
    }

    "should call getAllInvoices when no filter params are passed " in {
      (mockInvoicingService.getAllInvoices _).expects().returns(List())
      Get("/invoices") ~> invoiceServiceApi.routes ~> check{
        status should be (StatusCodes.OK)
        responseAs[String] should be ("The filter criteria yielded no results")
      }
    }
  }
}
