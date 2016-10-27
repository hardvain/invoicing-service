package com.acme.invoiceservice.exceptions

case class ApplicationException(message:String) extends Exception(message)