package com.acme.invoiceservice

import java.io.InputStreamReader
import spray.routing.RejectionHandler.Default
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, ActorRefFactory, ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigSyntax}
import spray.can.Http
import spray.routing.{HttpService, Route}

object InvoiceServiceApp extends App {
  implicit val system = ActorSystem("com-acme-invoiceservice")
  val config = ConfigFactory.load()
  private val invoiceServiceConfig: InvoiceServiceConfig = InvoiceServiceConfig(config)
  private val invoiceApiActor: ActorRef = system.actorOf(Props(classOf[InvoiceApi], invoiceServiceConfig), "invoice-api")
  IO(Http) ! Http.Bind(invoiceApiActor, invoiceServiceConfig.host, invoiceServiceConfig.port)
}

case class InvoiceServiceConfig(config: Config) {
  private val acme = config.getConfig("com.acme")
  val host = acme.getString("host")
  val port = acme.getInt("port")
}

case class InvoiceApi(config: InvoiceServiceConfig) extends Actor with HttpService with CustomerApi {
  override implicit def actorRefFactory: ActorRefFactory = context

  implicit val system = context.system

  override def receive: Receive = runRoute(customerRoutes)

}

trait CustomerApi {
  this: HttpService =>
  val customerRoutes: Route = {
    path("customers") {
      get {
        println("get customers")
        complete("get customers")
      }
    } ~
      path("customers" / Segment) { customerId =>
        get {
          println(s"get customer $customerId")
          complete(s"get customer $customerId")
        }
      } ~
      path("customers" / Segment) { customerId =>
        post {
          println(s"post customer $customerId")
          complete(s"post customer $customerId")
        }
      } ~
      path("customers" / Segment) { customerId =>
        delete {
          println(s"delete customer $customerId")
          complete(s"delete customer $customerId")
        }
      }
  }


}