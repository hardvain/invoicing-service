package com.acme.invoiceservice.models

import org.scalatest.{FreeSpec, Matchers}
import com.acme.invoiceservice.models.Invoice.InvoiceProtocol._
import org.joda.time.DateTime
import spray.json._
class InvoiceProtocolSpec extends FreeSpec with Matchers{
  "InvoiceProtocol"-{
    "should convert json to DateTime correctly" in {
      val time: DateTime = "2016-10-27T08:22:15.919+05:30".parseJson.convertTo[DateTime]
      assert(time != null)
    }

    "should throw error when given wrong json for date" in {
      val exception = intercept[DeserializationException]{
        """{"date":"2016"}""".parseJson.convertTo[DateTime]
      }

      assert(exception.getMessage == """{"date":"2016"} cannot be converted to date time""")
    }

    "should convert date time to string properly" in {
      val dateTime: DateTime = DateTime.now()
      val json: JsValue = dateTime.toJson
      assert(json == JsString(dateTime.toString()))
    }
  }
}
