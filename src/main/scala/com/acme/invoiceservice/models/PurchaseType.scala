package com.acme.invoiceservice.models

import com.acme.invoiceservice.exceptions.InvalidDataException

object PurchaseType extends Enumeration {
  val Shop = Value("shop")
  val Regular = Value("regular")
  type PropertyType = Value

  def parseString(string: String) = {
    string match {
      case "shop" => Shop
      case "regular" => Regular
      case _ => throw InvalidDataException("Unsupported purchase type")
    }
  }
}
