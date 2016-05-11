package org.krz

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source
import scala.util.Try

object Alior {

  case class Column(
    data: DateTime,
    odbiorca: String,
    rachunek: String,
    tytul: String,
    kwota: BigDecimal
  )

  val encoding = "UTF-8"
  val separator = ","
  val comma = "."
  val dateFormat = "dd-mm-YYYY"

  def main(args: Array[String]): Unit = {
    val input = args.lift(0).getOrElse(sys.error("Please provide input file as first argument"))
    val conversionRate: BigDecimal = args.lift(1).flatMap(x => Try(BigDecimal(x)).toOption).getOrElse(1)
    val convert = convertCurrency(conversionRate)

    println(Ynab.ynabColumns.mkString(Ynab.ynabSeparator))
    Source.fromFile(input, encoding).getLines()
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importColumns andThen reorganiseColumns andThen convert andThen exportColumns andThen println)
  }

  def cleanup(s: String) = s.replace("\"", "").replaceAll("\\s\\s*", " ").trim

  def cleanupNumeric(s: String) = Try(BigDecimal(
    s.replace(comma, ".").replaceAll("\\s", "").replace("PLN", "")
  )).getOrElse(sys.error(s"Could not parse numeric $s"))

  def importColumns = (data: String) => data.split(separator).toSeq.map(cleanup) match {
    case Seq(date, odbiorca, rachunek, tytul, kwota) =>
      Column(
        data = DateTime.parse(date, DateTimeFormat.forPattern(dateFormat)),
        odbiorca = odbiorca,
        rachunek = rachunek,
        tytul = tytul,
        kwota = cleanupNumeric(kwota)
      )
    case x => sys.error(s"invalid column format $x")
  }

  def reorganiseColumns = (column: Column) => Ynab.Column(
    // TODO reformat date
    date = column.data.toString(Ynab.dateFormat),
    payee = column.odbiorca,
    memo = column.tytul,
    inflow = column.kwota
  )

  def convertCurrency(rate: BigDecimal) = (column: Ynab.Column) =>
    column.copy(
      inflow = column.inflow * rate
    )

  def exportColumns = (column: Ynab.Column) => column.toCsv.mkString(Ynab.ynabSeparator)
}
