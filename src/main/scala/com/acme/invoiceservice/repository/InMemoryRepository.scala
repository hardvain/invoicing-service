package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter.Filter
import scala.collection.mutable.ListBuffer

class InMemoryRepository[A] extends Repository[A]{
  val entityBuffer = ListBuffer[A]()
  override def query(filter: Filter): List[A] = {
    List()
  }


  override def getAll: List[A] = {
    entityBuffer.toList
  }

  override def add(entity: A): Unit = {
    entityBuffer += entity
  }
}
