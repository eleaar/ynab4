package org.krz

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.io.Source

object Revolut {

  private case class Row(
    completedDate: LocalDate,
    reference: String,
    paidOut: Option[BigDecimal],
    paidIn: Option[BigDecimal],
    exchangeIn: Option[BigDecimal],
    exchangeOut: Option[BigDecimal],
    balance: BigDecimal,
    category: String,
    notes: String
  )

  private val encoding = "UTF-8"
  private val separator = ";"
  private val dateFormat = DateTimeFormatter.ofPattern("MMM d, yyyy")

  def main(args: Array[String]): Unit = {
    val input = args.headOption.getOrElse(sys.error("Please provide input file as first argument"))

    println(Ynab.ynabHeader)
    Source.fromFile(input, encoding).getLines()
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importRow andThen reorganiseColumns andThen renderYnabRow andThen println)
  }

  private def importRow = (data: String) => data.split(separator).toSeq.map(cleanupQuotes) match {
    case Seq(completedDate, reference, paidOut, paidIn, exchangeIn, exchangeOut, balance, category, notes) =>
//      println(data)
//      println(LocalDate.now().format(DateTimeFormatter.ofPattern("L dd, yyyy")))
//      println(LocalDate.now().format(DateTimeFormatter.ofPattern("LLL dd, yyyy")))
//      println(LocalDate.now().format(DateTimeFormatter.ofPattern("M dd, yyyy")))
//      println(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
      Row(
        completedDate = LocalDate.parse(completedDate, dateFormat),
        reference = reference,
        paidOut = notEmptyString(paidOut.trim).map(cleanupNumeric),
        paidIn = notEmptyString(paidIn.trim).map(cleanupNumeric),
        exchangeIn = None,
        exchangeOut = None,
        balance = cleanupNumeric(balance),
        category = category,
        notes = notes
      )
    case x => sys.error(s"unexpected row format $x")
  }

  private def reorganiseColumns = (row: Row) => Ynab.Row(
    date = row.completedDate,
    payee = row.reference,
    memo = row.reference,
    inflow = row.paidIn orElse row.paidOut.map(_ * -1) getOrElse 0
  )

}
