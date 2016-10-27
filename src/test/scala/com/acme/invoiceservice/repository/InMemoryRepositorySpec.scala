package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.Invoice
import org.joda.time.DateTime
import org.scalatest.{FreeSpec, Matchers}

class InMemoryRepositorySpec extends FreeSpec with Matchers {
  private val inMemoryRepository: InMemoryRepository[Invoice] = new InMemoryRepository[Invoice]
  "InMemoryRepository" - {
    "should fetch all items after adding items" in {
      val dateTime: DateTime = DateTime.now()
      val invoices = List(
        Invoice("1", "customer1", "address1", 1, "regular", "invoice", dateTime, dateTime, 1, dateTime, dateTime, "description", 10, 10, 10),
        Invoice("1", "customer2", "address2", 2, "regular", "invoice", dateTime, dateTime, 2, dateTime, dateTime, "description", 10, 10, 10),
        Invoice("1", "customer3", "address3", 3, "regular", "invoice", dateTime, dateTime, 3, dateTime, dateTime, "description", 10, 10, 10)
      )
      invoices.foreach(inMemoryRepository.add)
      val invoicesFromDb: List[Invoice] = inMemoryRepository.getAll
      assert(invoicesFromDb == invoices)
    }
  }
}
