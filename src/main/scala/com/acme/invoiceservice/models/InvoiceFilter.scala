package com.acme.invoiceservice.models

object InvoiceFilter {

  trait Filter

  case class AndFilter(filters: List[Filter]) extends Filter

  case class OrFilter(filters: List[Filter]) extends Filter

  case class NotFilter(filter: Filter)

  case class MatchFilter(field: String, value: Any)

  case class RangeFilter(field: String, gt: Option[Any], gte: Option[Any], lt: Option[Any], lte: Option[Any])
}
