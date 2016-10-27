package com.acme.invoiceservice.models

import com.acme.invoiceservice.exceptions.InvalidDataException
import org.joda.time.DateTime

/**
  * Filters an invoice collection dynamically. Used for demo purpose
  */
class InMemoryInvoiceFilter extends InMemoryEntityFilter[Invoice]{
 def constructFilter(fieldName: String, value: Any) : Invoice => Boolean = {
   fieldName match {
     case "invoiceId" => (invoice: Invoice) => invoice.invoiceId == value.toString
     case "customerId" => (invoice: Invoice) => invoice.customerId == value.toString
     case "address" => (invoice: Invoice) => invoice.address == value.toString
     case "month" => (invoice: Invoice) => invoice.month == value.toString.toInt
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
     case "purchaseType" => (invoice: Invoice) => invoice.purchaseType == PurchaseType.parseString(value.toString)
     case _ => throw InvalidDataException(s"Unknown filter $fieldName")
   }
 }
}


