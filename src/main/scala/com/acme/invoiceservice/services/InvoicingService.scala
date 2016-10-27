package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.models.InvoiceFilter._
import com.acme.invoiceservice.repository.{MongoRepository, Repository}

case class InvoicingService(repository: Repository[Invoice]) {
  def getInvoiceForFilters(filterMap: Map[String, String]): List[Invoice] = {
    val filter = Filter.fromMap(filterMap)
    repository.query(filter)
  }

  def getAllInvoices : List[Invoice] ={
    repository.getAll
  }

  def addInvoice(invoice: Invoice)={
    repository.add(invoice)
  }
}
