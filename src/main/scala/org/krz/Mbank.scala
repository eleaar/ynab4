package org.krz

import scala.io.Source

object Mbank {

  private val mbankEncoding = "Windows-1250"
  private val mbankSeparator = ";"
  private val dateFormat = "dd/mm/YYYY"

  def main(args: Array[String]): Unit = {
    val input = args.headOption.getOrElse(sys.error("Please provide input file as first argument"))

    println(Ynab.ynabHeader)
    Source.fromFile(input, mbankEncoding).getLines()
      .drop(37) // meta at the beginning
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importColumns andThen reorganiseColumns andThen renderYnabRow andThen println)
  }

  private def importColumns = (data: String) => data.split(mbankSeparator).toSeq.map(cleanupQuotes)

  private def reorganiseColumns = (column: Seq[String]) => {
    Ynab.Row(
      // TODO reformat date
      date = cleanupDate(column(0), dateFormat),
      payee = column(4),
      memo = column(3),
      inflow = cleanupNumeric(column(6))
    )
  }

}
