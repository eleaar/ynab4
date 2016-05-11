package org.krz

import scala.io.Source

object Mbank {

  val mbankEncoding = "Windows-1250"
  val mbankSeparator = ";"
  val mbankComma = ","

  def main(args: Array[String]): Unit = {
    val input = args.lift(0).getOrElse(sys.error("Please provide input file as first argument"))

    println(Ynab.ynabColumns.mkString(Ynab.ynabSeparator))
    Source.fromFile(input, mbankEncoding).getLines()
      .drop(37) // meta at the beginning
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importColumns andThen reorganiseColumns andThen exportColumns andThen println)
  }

  def cleanup(s: String) = s.replace("\"", "").replaceAll("\\s\\s*", " ").trim

  def cleanupNumeric(s: String) = BigDecimal(s.replace(",", ".").replaceAll("\\s", ""))

  def importColumns = (data: String) => data.split(mbankSeparator).toSeq.map(cleanup)

  def reorganiseColumns = (column: Seq[String]) => {
    Ynab.Column(
      // TODO reformat date
      date = column(0),
      payee = column(4),
      memo = column(3),
      inflow = cleanupNumeric(column(6))
    )
  }

  def exportColumns = (column: Ynab.Column) => column.toCsv.mkString(Ynab.ynabSeparator)
}
