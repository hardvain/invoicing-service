package com.acme.invoiceservice.models

/**
  * Base trait for any entity that wishes to apply a filter dynamically on it.
  * See `InMemoryInvoiceFilter` for an example
  * @tparam A
  */
trait InMemoryEntityFilter[A] {

  /**
    * Any entity should check for the field name and return a corresponding predicate function
    *
    * @param fieldName
    * @param value
    * @return
    */
  def constructFilter(fieldName: String, value: Any): A => Boolean
}
