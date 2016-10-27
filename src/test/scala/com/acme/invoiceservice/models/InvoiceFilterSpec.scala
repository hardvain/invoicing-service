package com.acme.invoiceservice.models

import com.acme.invoiceservice.models.InvoiceFilter._
import org.scalatest.FreeSpec

class InvoiceFilterSpec extends FreeSpec {
  "Filter" - {
    "should convert Map[String,String] with more than one value into AndFilter of MatchFilters" in {
      val filter: Filter = Filter.fromMap(Map("name" -> "vanderbron", "country" -> "netherlands"))
      assert(filter === AndFilter(List(MatchFilter("name","vanderbron"), MatchFilter("country","netherlands"))))
    }

    "should convert Map[String,String] with only one value into MatchFilter" in {
      val filter: Filter = Filter.fromMap(Map("name" -> "vanderbron"))
      assert(filter === MatchFilter("name","vanderbron"))
    }
  }
}
