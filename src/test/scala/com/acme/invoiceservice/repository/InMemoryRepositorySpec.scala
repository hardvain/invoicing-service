package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter.{AndFilter, MatchFilter, NotFilter, OrFilter}
import com.acme.invoiceservice.models.{InMemoryInvoiceFilter, Invoice, PurchaseType}
import org.joda.time.DateTime
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}

class InMemoryRepositorySpec extends FreeSpec with Matchers with MockFactory {
  private val inMemoryInvoiceFilter: InMemoryInvoiceFilter = new InMemoryInvoiceFilter
  private val inMemoryRepository: InMemoryRepository[Invoice] = new InMemoryRepository[Invoice](inMemoryInvoiceFilter)
  val dateTime: DateTime = DateTime.now()
  "InMemoryRepository" - {
    "get all" - {
      "should fetch all items after adding items" in {
        val invoices = List(
          Invoice("1", "customer1", "address1", 1, PurchaseType.Regular, "invoice", dateTime, dateTime, 1, dateTime, dateTime, "description", 10, 10, 10),
          Invoice("2", "customer2", "address2", 2, PurchaseType.Regular, "invoice", dateTime, dateTime, 2, dateTime, dateTime, "description", 10, 10, 10),
          Invoice("3", "customer3", "address3", 3, PurchaseType.Regular, "invoice", dateTime, dateTime, 3, dateTime, dateTime, "description", 10, 10, 10)
        )
        invoices.foreach(inMemoryRepository.add)
        val invoicesFromDb: List[Invoice] = inMemoryRepository.getAll
        assert(invoicesFromDb == invoices)
      }
    }
    "query" - {
        
         val invoices =  List(
            Invoice("1", "customer1", "address1", 1, PurchaseType.Shop, "invoice", dateTime, dateTime, 1, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("2", "customer2", "address2", 2, PurchaseType.Regular, "invoice", dateTime, dateTime, 2, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("3", "customer3", "address3", 3, PurchaseType.Shop, "invoice", dateTime, dateTime, 3, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("4", "customer4", "address4", 4, PurchaseType.Regular, "invoice", dateTime, dateTime, 4, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("5", "customer5", "address5", 5, PurchaseType.Regular, "invoice", dateTime, dateTime, 5, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("6", "customer6", "address6", 6, PurchaseType.Regular, "invoice", dateTime, dateTime, 6, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("7", "customer7", "address7", 7, PurchaseType.Regular, "invoice", dateTime, dateTime, 7, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("8", "customer8", "address8", 8, PurchaseType.Regular, "invoice", dateTime, dateTime, 8, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("9", "customer9", "address9", 9, PurchaseType.Regular, "invoice", dateTime, dateTime, 9, dateTime, dateTime, "description", 10, 10, 10),
            Invoice("10", "customer10", "address10", 10, PurchaseType.Regular, "invoice", dateTime, dateTime, 10, dateTime, dateTime, "description", 10, 10, 10)
          )

      "should apply AndFilter correctly" in {
        invoices.foreach(inMemoryRepository.add)
        val filterResult: List[Invoice] = inMemoryRepository.query(AndFilter(List(MatchFilter("purchaseType", "shop"), MatchFilter("customerId", "customer1"))))
        assert(filterResult.size == 1)
        inMemoryRepository.clear()
      }

      "should apply OrFilter correctly" in {
        invoices.foreach(inMemoryRepository.add)
        val filterResult: List[Invoice] = inMemoryRepository.query(OrFilter(List(MatchFilter("purchaseType", "shop"), MatchFilter("customerId", "customer1"))))
        assert(filterResult.size == 2)
        inMemoryRepository.clear()
      }
      "should apply NotFilter correctly" in {
        invoices.foreach(inMemoryRepository.add)
        val filterResult: List[Invoice] = inMemoryRepository.query(NotFilter(AndFilter(List(MatchFilter("purchaseType", "shop"), MatchFilter("customerId", "customer1")))))
        assert(filterResult.size == 9)
        inMemoryRepository.clear()
      }
      "should apply MatchFilter correctly" in {
        invoices.foreach(inMemoryRepository.add)
        val filterResult: List[Invoice] = inMemoryRepository.query(MatchFilter("purchaseType", "shop"))
        assert(filterResult.size == 2)
        inMemoryRepository.clear()

      }
    }
  }

}
