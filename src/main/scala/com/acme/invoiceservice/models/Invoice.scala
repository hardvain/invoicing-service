package com.acme.invoiceservice.models

import spray.http.DateTime
import spray.json._
import DefaultJsonProtocol._
case class Invoice(
                    invoiceId: String, customerId: String, address: String, month: Int, invoiceType: String,
                    invoiceTypeLocalized: String, invoiceDate: String, paymentDueDate: String, invoiceNumber: Int,
                    startDate: String, endDate: String, periodDescription: String, amount: Double, vatAmount: Double,
                    totalAmount: Double)

object InvoiceProtocol extends DefaultJsonProtocol{
  implicit val sprayFormat = jsonFormat15(Invoice.apply)
}