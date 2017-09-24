package org.krz

import org.joda.time.DateTime

import scala.io.Source

object SocieteGenerale {

  private case class Row(
    date: DateTime,
    libelle: String,
    detail: String,
    montant: BigDecimal,
    devise: String
  )

  private val encoding = "Windows-1250"
  private val separator = ";"
  private val dateFormat = "dd/mm/YYYY"

  def main(args: Array[String]): Unit = {
    val input = args.headOption.getOrElse(sys.error("Please provide input file as first argument"))

    println(Ynab.ynabHeader)
    Source.fromFile(input, encoding).getLines()
      .drop(2) // meta at the beginning
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importRow andThen reorganiseColumns andThen renderYnabRow andThen println)
  }

  private def importRow = (data: String) => data.split(separator).toSeq.map(cleanupQuotes) match {
    case Seq(date, libelle, detail, montant, devise) =>
      Row(
        date = cleanupDate(date, dateFormat),
        libelle = libelle,
        detail = detail,
        montant = cleanupNumeric(montant),
        devise = devise
      )
    case x => sys.error(s"unexpected row format $x")
  }

  private def reorganiseColumns = (row: Row) => Ynab.Row(
    date = row.date,
    payee = row.detail,
    memo = row.detail,
    inflow = row.montant
  )

}
