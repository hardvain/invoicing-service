package com.acme.invoiceservice.api

import akka.actor.ActorRefFactory
import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.services.InvoicingService
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import spray.testkit.ScalatestRouteTest
import org.scalatest.mock.MockitoSugar
import spray.http.StatusCodes

class InvoiceServiceApiSpec extends FreeSpec with ScalatestRouteTest with Matchers with MockFactory {
  class InvoicingServiceStub extends InvoicingService(null, null)
  private val mockInvoicingService: InvoicingService = mock[InvoicingServiceStub]
  val context = mock[ActorRefFactory]

  private val invoiceServiceApi: InvoiceServiceApi = InvoiceServiceApi(null, mockInvoicingService, context)
  "Invoices" - {
    "should return 200 OK when a GET request is made " in {
      val invoices = List(
        Invoice("1","customer1","address1",1,"regular","invoice","date","date",1,"date","date","description",10,10,10),
        Invoice("1","customer1","address1",1,"regular","invoice","date","date",1,"date","date","description",10,10,10),
        Invoice("1","customer1","address1",1,"regular","invoice","date","date",1,"date","date","description",10,10,10)
      )
      (mockInvoicingService.getInvoiceForFilters _).expects(Map[String,String]()).returns(invoices)
        Get("/invoices") ~> invoiceServiceApi.routes ~> check{
          status should be (StatusCodes.OK)
        }
    }

    "should return appropriate message when search results are empty " in {
      (mockInvoicingService.getInvoiceForFilters _).expects(Map[String,String]()).returns(List[Invoice]())
      Get("/invoices") ~> invoiceServiceApi.routes ~> check{
        status should be (StatusCodes.OK)
        responseAs[String] should be ("The filter criteria yielded no results")
      }
    }
  }
}
