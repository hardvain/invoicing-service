package com.acme.invoiceservice.models

import com.acme.invoiceservice.models.PurchaseType.PropertyType
import org.joda.time.DateTime
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

case class Invoice(
                    invoiceId: String, customerId: String, address: String, month: Int, purchaseType: PurchaseType.PropertyType,
                    invoiceTypeLocalized: String, invoiceDate: DateTime, paymentDueDate: DateTime, invoiceNumber: Int,
                    startDate: DateTime, endDate: DateTime, periodDescription: String, amount: Double, vatAmount: Double,
                    totalAmount: Double)


object Invoice{

  /**
    * Custom Spray Protocol to help in automatic serialization and deserialization between `Invoice` and Json
    */
  object InvoiceProtocol extends DefaultJsonProtocol{
    implicit val dateTimeFormat : RootJsonFormat[DateTime] = new RootJsonFormat[DateTime] {
      override def read(json: JsValue): DateTime = {
        json match {
          case JsString(value) => DateTime.parse(value)
          case _ => throw DeserializationException(s"$json cannot be converted to date time")
        }
      }

      override def write(obj: DateTime): JsValue = {
        JsString(obj.toString())
      }
    }
    implicit val purchaseTypeFormat : RootJsonFormat[PurchaseType.PropertyType] = new RootJsonFormat[PropertyType] {
      override def read(json: JsValue): PropertyType = {
        json match {
          case JsString(value) => PurchaseType.parseString(value)
          case _ => throw DeserializationException(s"$json cannot be converted to date time")
        }
      }

      override def write(obj: PropertyType): JsValue = {
        JsString(obj.toString)
      }
    }

    implicit val sprayFormat = jsonFormat15(Invoice.apply)
  }

}
