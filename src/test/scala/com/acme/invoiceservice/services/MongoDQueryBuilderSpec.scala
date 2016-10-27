package com.acme.invoiceservice.services

import com.acme.invoiceservice.models.InvoiceFilter._
import org.mongodb.scala.bson.conversions.Bson
import org.scalatest.{FreeSpec, Matchers}

/**
  * The method of testing used in this test is fragile. Asserting against strings. Because refactoring the logic will
  * not result in the expected output getting changed automatically. Unfortunately MongoDB does not provide a way to
  * assert the state of BSON. Ideally this should have been an integration test where each test inserts data into
  * MongoDB, runs the query and asserts on the result. Since we will be asserting on the result which will be
  * a list of Invoice objects, refactoring the builders or filters can be made in a deterministic manner.
  */
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

      "with AND filter" - {
        "should combine primitive filters" in {
          val filter: AndFilter = AndFilter(List(MatchFilter("field1","value1"), MatchFilter("field2","value2")))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          println(bson)
          assert(bson.toString ==
            "And Filter{filters=[Filter{fieldName='field1', value=value1}, Filter{fieldName='field2', value=value2}]}")
        }
      }
      "with OR filter" - {
        "should combine primitive filters" in {
          val filter: OrFilter = OrFilter(List(MatchFilter("field1","value1"), MatchFilter("field2","value2")))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          println(bson)
          assert(bson.toString ==
            "Or Filter{filters=[Filter{fieldName='field1', value=value1}, Filter{fieldName='field2', value=value2}]}")
        }
      }
      "with NOT filter" - {
        "should combine primitive filters" in {
          val filter: NotFilter = NotFilter(MatchFilter("field1","value1"))
          val bson: Bson = mongoDBQueryBuilder.build(filter)
          println(bson)

          assert(bson.toString ==
            "Not Filter{filter=Filter{fieldName='field1', value=value1}}")
        }
      }
    }
  }
}
