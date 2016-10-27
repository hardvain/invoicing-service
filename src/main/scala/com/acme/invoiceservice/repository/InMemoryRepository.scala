package com.acme.invoiceservice.repository

import com.acme.invoiceservice.models.InMemoryEntityFilter
import com.acme.invoiceservice.models.InvoiceFilter._
import scala.collection.mutable.ListBuffer

/**
  * Dummy in memory repository for entities of type `A`. Parameterized over type `A`.
  * For demo purposes this stores the entities in memory.
  * Also for demo purposes, this involves few methods that helps in filtering values from the collection dynamically
  */
class InMemoryRepository[A](inMemoryEntityFilter: InMemoryEntityFilter[A]) extends Repository[A] {
  val entityBuffer = ListBuffer[A]()

  override def query(filter: Filter): List[A] = {
    // No implementation is provided here because InMemoryRepository is for Demo purpose
    applyFilter(filter)
  }

  override def getAll: List[A] = {
    entityBuffer.toList
  }

  override def add(entity: A): Unit = {
    entityBuffer += entity
  }

  def clear() = entityBuffer.clear()
  private def applyFilter(filter: Filter): List[A] = {
    filter match {
      case filter: AndFilter => applyAndFilter(filter)
      case filter: OrFilter => applyOrFilter(filter)
      case filter: NotFilter => applyNotFilter(filter)
      case filter: MatchFilter => applyMatchFilter(filter)
    }
  }

  private def applyMatchFilter(matchFilter: MatchFilter) = {
    val predicate: (A) => Boolean = inMemoryEntityFilter.constructFilter(matchFilter.field, matchFilter.value)
    entityBuffer.filter(predicate).toList
  }

  private def applyAndFilter(andFilter: AndFilter) = {
    andFilter.filters.map(filter => applyFilter(filter).toSet).reduceLeft((s1,s2) => s1.intersect(s2)).toList
  }

  private def applyOrFilter(orFilter: OrFilter) = {
    orFilter.filters.map(filter => applyFilter(filter).toSet).reduceLeft((s1,s2) => s1.union(s2)).toList
  }

  private def applyNotFilter(notFilter: NotFilter) = {
    val matchingSet: Set[A] = applyFilter(notFilter.filter).toSet
    entityBuffer.toSet.diff(matchingSet).toList
  }
}
