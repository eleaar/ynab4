package org.krz


import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source

object SocieteGenerale {

  case class Column(
    date: DateTime,
    libelle: String,
    detail: String,
    montant: BigDecimal,
    devise: String
  )

  val encoding = "Windows-1250"
  val separator = ";"
  val comma = ","
  val dateFormat = "dd/mm/YYYY"

  def main(args: Array[String]): Unit = {
    val input = args.lift(0).getOrElse(sys.error("Please provide input file as first argument"))

    println(Ynab.ynabColumns.mkString(Ynab.ynabSeparator))
    Source.fromFile(input, encoding).getLines()
      .drop(2) // meta at the beginning
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importColumns andThen reorganiseColumns andThen exportColumns andThen println)
  }

  def cleanup(s: String) = s.replace("\"", "").replaceAll("\\s\\s*", " ").trim

  def cleanupNumeric(s: String) = BigDecimal(s.replace(",", ".").replaceAll("\\s", ""))

  def importColumns = (data: String) => data.split(separator).toSeq.map(cleanup) match {
    case Seq(date, libelle, detail, montant, devise) =>
      Column(
        date = DateTime.parse(date, DateTimeFormat.forPattern(dateFormat)),
        libelle = libelle,
        detail = detail,
        montant = cleanupNumeric(montant),
        devise = devise
      )
    case x => sys.error(s"invalid column format $x")
  }

  def reorganiseColumns = (column: Column) => Ynab.Column(
    // TODO reformat date
    date = column.date.toString(Ynab.dateFormat),
    payee = column.detail,
    memo = column.detail,
    inflow = column.montant
  )

  def exportColumns = (column: Ynab.Column) => column.toCsv.mkString(Ynab.ynabSeparator)
}
