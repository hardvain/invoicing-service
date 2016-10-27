package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter.Filter

trait Repository[A] {

  def query(filter:Filter): List[A]

  def getAll: List[A]

  def add(a: A)
}
