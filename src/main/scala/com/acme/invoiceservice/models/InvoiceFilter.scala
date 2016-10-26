package com.acme.invoiceservice.models

object InvoiceFilter {

  trait Filter

  object Filter {
    def fromMap(map: Map[String, String]): Filter = {
      map.size match {
        case 1 =>
          val head: (String, String) = map.head
          MatchFilter(head._1, head._2)
        case _ =>
          val matchFilters = for ((key, value) <- map) yield MatchFilter(key, value)
          AndFilter(matchFilters.toList)
      }
    }
  }

  case class AndFilter(filters: List[Filter]) extends Filter

  case class OrFilter(filters: List[Filter]) extends Filter

  case class NotFilter(filter: Filter) extends Filter

  case class MatchFilter(field: String, value: Any) extends Filter

  case class RangeFilter(
                          field: String, gt: Option[Any] = None, gte: Option[Any] = None, lt: Option[Any] = None,
                          lte: Option[Any] = None) extends Filter

}
