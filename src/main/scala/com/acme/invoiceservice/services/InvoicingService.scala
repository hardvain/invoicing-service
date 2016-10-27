package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.models.InvoiceFilter._
import com.acme.invoiceservice.repository.Repository

/**
  * Contacts the Invoicing repository and does the operations of filtering, adding and getting all invoices
  * @param repository
  */
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
