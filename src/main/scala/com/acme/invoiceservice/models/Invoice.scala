package com.acme.invoiceservice.models

import spray.http.DateTime

case class Invoice(
                    invoiceId: String, customerId: String, address: String, month: Int, invoiceType: String,
                    invoiceTypeLocalized: String, invoiceDate: DateTime, paymentDueDate: DateTime, invoiceNumber: Int,
                    startDate: DateTime, endDate: DateTime, periodDescription: String, amount: Double, vatAmount: Double,
                    totalAmount: Double)
