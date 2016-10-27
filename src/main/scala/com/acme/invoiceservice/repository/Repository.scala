package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InvoiceFilter.Filter


/**
  * Base trait for any class that deals with persistence of an entity.
  * @tparam A
  */
trait Repository[A] {

  def query(filter:Filter): List[A]

  def getAll: List[A]

  def add(a: A)
}
