package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter.Filter
import com.acme.invoiceservice.services.MongoDBQueryBuilder
import org.mongodb.scala.bson.conversions.Bson

class MongoRepository[A] extends Repository[A] {
  private val builder: MongoDBQueryBuilder = new MongoDBQueryBuilder
  override def query(filter:Filter): List[A] = {
    val bsonFilter: Bson = builder.build(filter)
    List()
  }

  override def getAll: List[A] = {
    List()
  }


  override def add(a: A): Unit = {

  }
}




