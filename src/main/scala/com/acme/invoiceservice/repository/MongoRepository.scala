package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter.Filter
import com.acme.invoiceservice.services.MongoDBQueryBuilder
import org.mongodb.scala.bson.conversions.Bson

/**
  * This layer is not unit tested. Ideally it should be covered by an integration test talking directly to a MongoDB
  * @tparam A
  */
class MongoRepository[A] extends Repository[A] {
  private val builder: MongoDBQueryBuilder = new MongoDBQueryBuilder
  override def query(filter:Filter): List[A] = {
    val bsonFilter: Bson = builder.build(filter)
    // use the above bson filter to query from dynamo db
    List()
  }

  override def getAll: List[A] = {
    // get all records from dynamo db
    List()
  }


  override def add(a: A): Unit = {
    // save the entity to dynamodb
  }
}




