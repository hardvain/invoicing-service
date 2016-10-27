package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.InvoiceFilter._
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters._

import scala.collection.mutable.ListBuffer

class MongoDBQueryBuilder {
  def build(invoiceFilter: Filter): Bson = {
    invoiceFilter match {
      case filter: AndFilter => buildFromAndFilter(filter)
      case filter: OrFilter => buildFromOrFilter(filter)
      case filter: NotFilter => buildFromNotFilter(filter)
      case filter: MatchFilter => buildFromMatchFilter(filter)
    }
  }

  private def buildFromMatchFilter(matchFilter: MatchFilter): Bson = {
    equal(matchFilter.field, matchFilter.value)
  }

  private def buildFromAndFilter(andFilter: AndFilter) = {
    and(andFilter.filters.map(build): _*)
  }

  private def buildFromOrFilter(orFilter: OrFilter) = {
    or(orFilter.filters.map(build): _*)
  }

  private def buildFromNotFilter(notFilter: NotFilter) = {
    not(build(notFilter.filter))
  }

}
