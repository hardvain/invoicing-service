package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.InvoiceFilter._
import com.mongodb.{DBObject, QueryBuilder}
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
      case filter: RangeFilter => buildFromRangeFilter(filter)
    }
  }

  private def buildFromMatchFilter(matchFilter: MatchFilter): Bson = {
    equal(matchFilter.field, matchFilter.value)
  }

  private def buildFromRangeFilter(rangeFilter: RangeFilter): Bson = {
    val field = rangeFilter.field
    val bsonList = ListBuffer[Bson]()
    rangeFilter.gt.map(gtValue => bsonList += gt(field, gtValue))
    rangeFilter.gte.map(gteValue => bsonList += gt(field, gteValue))
    rangeFilter.lt.map(ltValue => bsonList += gt(field, ltValue))
    rangeFilter.lte.map(lteValue => bsonList += gt(field, lteValue))
    and(bsonList:_*)
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
