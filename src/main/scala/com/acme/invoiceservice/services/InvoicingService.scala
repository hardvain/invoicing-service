package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.Invoice
import com.acme.invoiceservice.models.InvoiceFilter._
import com.acme.invoiceservice.repository.MongoRepository

case class InvoicingService(mongoDBQueryBuilder: MongoDBQueryBuilder, mongoRepository: MongoRepository) {
  def getInvoiceForFilters(filterMap: Map[String, String]): List[Invoice] = {
    val filter = Filter.fromMap(filterMap)
    val invoiceMongoFilter = mongoDBQueryBuilder.build(filter)
    mongoRepository.query(invoiceMongoFilter)
  }

  def getAllInvoices : List[Invoice] ={
    mongoRepository.getAll
  }
}
