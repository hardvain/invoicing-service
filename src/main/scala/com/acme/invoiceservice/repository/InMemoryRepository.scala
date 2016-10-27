package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter._

import scala.collection.mutable.ListBuffer

class InMemoryRepository[A] extends Repository[A]{
  val entityBuffer = ListBuffer[A]()
  override def query(filter: Filter): List[A] = {
    // No implementation is provided here because InMemoryRepository is for Demo purpose
    List()
  }

  override def getAll: List[A] = {
    entityBuffer.toList
  }

  override def add(entity: A): Unit = {
    entityBuffer += entity
  }
}
