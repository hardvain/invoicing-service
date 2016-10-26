package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.InvoiceFilter._
import org.mongodb.scala.bson.conversions.Bson
import org.scalatest.{FreeSpec, Matchers}

class MongoDQueryBuilderSpec extends FreeSpec with Matchers{
  private val mongoDBQueryBuilder: MongoDBQueryBuilder = new MongoDBQueryBuilder
  "MongoDBQueryBuilder" - {
    "on build" -{
      "with match filter" -{
        "should return match json " in {
          val bson: Bson = mongoDBQueryBuilder.build(MatchFilter("field","value"))
          assert(bson.toString == "Filter{fieldName='field', value=value}")
        }
      }
      "with range filter" -{
        "with  gt value should return range bson" in {
          val bson: Bson = mongoDBQueryBuilder.build(RangeFilter("field",Some(1)))
          println(bson)
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$gt', value=1}]}")
        }
        "with  gte value should return range bson" in {
          val bson: Bson = mongoDBQueryBuilder.build(RangeFilter("field",gte = Some(1)))
          println(bson)
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$gte', value=1}]}")
        }

        "with  lt value should return range bson" in {
          val bson: Bson = mongoDBQueryBuilder.build(RangeFilter("field",lt = Some(1)))
          println(bson)
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$lt', value=1}]}")
        }

        "with  lte value should return range bson" in {
          val bson: Bson = mongoDBQueryBuilder.build(RangeFilter("field",lte = Some(1)))
          println(bson)
          assert(bson.toString == "And Filter{filters=[Operator Filter{fieldName='field', operator='$lte', value=1}]}")
        }
      }

    }
  }
}
