package com.acme.invoiceservice.models

import com.acme.invoiceservice.exceptions.InvalidDataException
import org.scalatest.{FreeSpec, Matchers}

class PurchaseTypeSpec extends FreeSpec with Matchers {
  "PurchaseType" - {
    "parseString" - {
      "should convert valid string to purchase type" in {
        assert(PurchaseType.parseString("shop") == PurchaseType.Shop)
        assert(PurchaseType.parseString("regular") == PurchaseType.Regular)
      }
      "should throw invalid data exception for invalid purchase type" in {
        val exception = intercept[InvalidDataException] {
          PurchaseType.parseString("random") == PurchaseType.Shop
        }

        assert(exception.message == "Unsupported purchase type")
      }
    }
  }

}
