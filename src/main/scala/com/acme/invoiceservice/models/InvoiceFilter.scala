package com.acme.invoiceservice.models

object InvoiceFilter {

  trait Filter

  case class AndFilter(filters: List[Filter]) extends Filter

  case class OrFilter(filters: List[Filter]) extends Filter

  case class NotFilter(filter: Filter) extends Filter

  case class MatchFilter(field: String, value: Any) extends Filter

  case class RangeFilter(field: String, gt: Option[Any], gte: Option[Any], lt: Option[Any], lte: Option[Any]) extends Filter
}
