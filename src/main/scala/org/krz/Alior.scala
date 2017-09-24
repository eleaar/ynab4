package org.krz

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.io.Source
import scala.util.Try

object Alior {

  private case class Row(
    data: LocalDate,
    odbiorca: String,
    rachunek: String,
    tytul: String,
    kwota: BigDecimal
  )

  private val encoding = "UTF-8"
  private val separator = ","
  private val comma = "."
  private val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")

  def main(args: Array[String]): Unit = {
    val input = args.headOption.getOrElse(sys.error("Please provide input file as first argument"))
    val conversionRate: BigDecimal = args.lift(1).flatMap(x => Try(BigDecimal(x)).toOption).getOrElse(1)
    val convert = convertCurrency(conversionRate)

    println(Ynab.ynabHeader)
    Source.fromFile(input, encoding).getLines()
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importColumns andThen reorganiseColumns andThen convert andThen renderYnabRow andThen println)
  }

  private def cleanupNumeric(s: String) = Try(BigDecimal(
    s.replace(comma, ".").replaceAll("\\s", "").replace("PLN", "")
  )).getOrElse(sys.error(s"Could not parse numeric $s"))

  private def importColumns = (data: String) => data.split(separator).toSeq.map(cleanupQuotes) match {
    case Seq(date, odbiorca, rachunek, tytul, kwota) =>
      Row(
        data = LocalDate.parse(date, dateFormat),
        odbiorca = odbiorca,
        rachunek = rachunek,
        tytul = tytul,
        kwota = cleanupNumeric(kwota)
      )
    case x => sys.error(s"invalid column format $x")
  }

  private def reorganiseColumns = (column: Row) => Ynab.Row(
    // TODO reformat date
    date = column.data,
    payee = column.odbiorca,
    memo = column.tytul,
    inflow = column.kwota
  )

  private def convertCurrency(rate: BigDecimal) = (column: Ynab.Row) =>
    column.copy(
      inflow = column.inflow * rate
    )

}
