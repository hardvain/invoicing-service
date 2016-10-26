package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.InvoiceFilter._
import org.mongodb.scala.bson.conversions.Bson
import org.scalatest.{FreeSpec, Matchers}

class MongoDQueryBuilderSpec extends FreeSpec with Matchers {
  private val mongoDBQueryBuilder: MongoDBQueryBuilder = new MongoDBQueryBuilder
  "MongoDBQueryBuilder" - {
    "on build" - {
      "with MATCH filter" - {
        "should return match json " in {
          val filter: MatchFilter = MatchFilter("field", "value")
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          assert(bson.toString == "Filter{fieldName='field', value=value}")
        }
      }
      "with RANGE filter" - {
        "with  gt value should return range bson" in {
          val filter: RangeFilter = RangeFilter("field", Some(1))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$gt', value=1}]}")
        }
        "with  gte value should return range bson" in {
          val filter: RangeFilter = RangeFilter("field", gte = Some(1))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$gte', value=1}]}")
        }

        "with  lt value should return range bson" in {
          val filter: RangeFilter = RangeFilter("field", lt = Some(1))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$lt', value=1}]}")
        }

        "with  lte value should return range bson" in {
          val filter: RangeFilter = RangeFilter("field", lte = Some(1))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$lte', value=1}]}")
        }
      }
      "with AND filter" - {
        "should combine primitive filters" in {
          val filter: AndFilter = AndFilter(List(RangeFilter("field", lte = Some(1)), MatchFilter("field1","value1")))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString ==
            "And Filter{filters=[And Filter{filters=[Operator Filter{fieldName='field', operator='$lte', value=1}]}, Filter{fieldName='field1', value=value1}]}")
        }
      }
      "with OR filter" - {
        "should combine primitive filters" in {
          val filter: OrFilter = OrFilter(List(RangeFilter("field", lte = Some(1)), MatchFilter("field1","value1")))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString ==
            "Or Filter{filters=[And Filter{filters=[Operator Filter{fieldName='field', operator='$lte', value=1}]}, Filter{fieldName='field1', value=value1}]}")
        }
      }
      "with NOT filter" - {
        "should combine primitive filters" in {
          val filter: NotFilter = NotFilter(RangeFilter("field", lte = Some(1)))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          
          assert(bson.toString ==
            "Not Filter{filter=And Filter{filters=[Operator Filter{fieldName='field', operator='$lte', value=1}]}}")
        }
      }
    }
  }
}
