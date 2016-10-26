package com.acme.invoiceservice.api

import akka.actor.{Actor, ActorRefFactory}
import com.acme.invoiceservice.InvoiceServiceConfig
import spray.routing._

case class InvoiceServiceApi(config: InvoiceServiceConfig) extends Actor with HttpService {
  override implicit def actorRefFactory: ActorRefFactory = context

  implicit val system = context.system

  override def receive: Receive = runRoute(invoiceRoutes)



  val invoiceRoutes: Route = {
    path("invoices") {
      get {
        parameterMap { params =>
          complete(params.toString())
        }
      }
    }
  }
}
