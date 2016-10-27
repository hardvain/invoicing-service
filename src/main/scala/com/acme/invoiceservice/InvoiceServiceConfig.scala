package com.acme.invoiceservice

import com.typesafe.config.Config

case class InvoiceServiceConfig(config: Config) {
  private val acme = config.getConfig("com.acme")
  val host = acme.getString("host")
  val port = acme.getInt("port")
  val isInMemory = acme.getBoolean("isInMemory")
}
