package com.acme.invoiceservice.exceptions

case class InvalidDataException(message:String) extends Exception(message)
