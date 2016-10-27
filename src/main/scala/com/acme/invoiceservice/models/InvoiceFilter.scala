package com.acme.invoiceservice.models


/**
  * Container of all filter classes
  */
object InvoiceFilter {

  /**
    * Base trait for representing a filter
    */
  trait Filter

  object Filter {
    /**
      * For demo purposes all the values in the `map` are considered to be `MatchFilter`s and all of them are combined
      * with `AndFilter`. Another possible implementation could be to use `OrFilter` to combine the values in the `map`
      * @param map
      * @return
      */
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

  /**
    * Combines multiple filters and provides a logical AND over all of them.
    * Returns only those results that satisfy all filters
    * @param filters
    */
  case class AndFilter(filters: List[Filter]) extends Filter

  /**
    * Combines multiple filters and provides a logical OR over all of them.
    * Returns only those results that satisfy atleast one of the filters
    * @param filters
    */
  case class OrFilter(filters: List[Filter]) extends Filter

  /**
    * Does a logical NOT over the inner filter.
    * Returns results that do not match the passed filter
    */
  case class NotFilter(filter: Filter) extends Filter


  /**
    * Primitive filter that checks a field for a specific value.
    * Returns results only those match the filter
    * @param field
    * @param value
    */
  case class MatchFilter(field: String, value: Any) extends Filter

}
