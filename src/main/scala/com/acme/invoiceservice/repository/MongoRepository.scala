package com.acme.invoiceservice.repository

import org.mongodb.scala.bson.conversions.Bson

class MongoRepository {
  def query[A](bson: Bson): List[A] = {
    List()
  }

  def getAll[A]() : List[A] ={
    List()
  }
}
