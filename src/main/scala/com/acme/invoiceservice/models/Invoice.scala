package com.acme.invoiceservice.models

import org.joda.time.DateTime
import spray.json._
case class Invoice(
                    invoiceId: String, customerId: String, address: String, month: Int, invoiceType: String,
                    invoiceTypeLocalized: String, invoiceDate: DateTime, paymentDueDate: DateTime, invoiceNumber: Int,
                    startDate: DateTime, endDate: DateTime, periodDescription: String, amount: Double, vatAmount: Double,
                    totalAmount: Double)


 class InMemoryInvoiceFilter extends InMemoryEntityFilter[Invoice]{
  def constructFilter(fieldName: String, value: Any) : Invoice => Boolean = {
    fieldName match {
      case "invoiceId" => (invoice: Invoice) => invoice.invoiceId == value.toString
      case "customerId" => (invoice: Invoice) => invoice.customerId == value.toString
      case "address" => (invoice: Invoice) => invoice.address == value.toString
      case "month" => (invoice: Invoice) => invoice.month == value.toString.toInt
      case "invoiceType" => (invoice: Invoice) => invoice.invoiceType == value.toString
      case "invoiceTypeLocalized" => (invoice: Invoice) => invoice.invoiceTypeLocalized == value.toString
      case "invoiceDate" => (invoice: Invoice) => invoice.invoiceDate == DateTime.parse(value.toString)
      case "paymentDueDate" => (invoice: Invoice) => invoice.paymentDueDate == DateTime.parse(value.toString)
      case "invoiceNumber" => (invoice: Invoice) => invoice.invoiceNumber == value.toString.toInt
      case "startDate" => (invoice: Invoice) => invoice.startDate == DateTime.parse(value.toString)
      case "endDate" => (invoice: Invoice) => invoice.endDate == DateTime.parse(value.toString)
      case "periodDescription" => (invoice: Invoice) => invoice.periodDescription == value.toString
      case "amount" => (invoice: Invoice) => invoice.amount == value.toString.toDouble
      case "vatAmount" => (invoice: Invoice) => invoice.vatAmount == value.toString.toDouble
      case "totalAmount" => (invoice: Invoice) => invoice.totalAmount == value.toString.toDouble
      case _ => throw new Exception(s"Unknown filter $fieldName")
    }
  }
}

trait InMemoryEntityFilter[A]{
  def constructFilter(fieldName: String, value: Any):A => Boolean
}

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
  implicit val sprayFormat = jsonFormat15(Invoice.apply)
}