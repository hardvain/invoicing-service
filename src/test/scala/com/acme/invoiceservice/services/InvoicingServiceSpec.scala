package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.InvoiceFilter._
import org.mongodb.scala.bson.conversions.Bson
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}

class InvoicingServiceSpec extends FreeSpec with Matchers with MockFactory{
  "getInvoiceForFilters" - {
    "sample" in {
      val builder: MongoDBQueryBuilder = new MongoDBQueryBuilder
      val build: Bson = builder.build(AndFilter(List(MatchFilter("name","vanderbron"), MatchFilter("address","netherlands"))))
      println(build)
    }
  }
}
