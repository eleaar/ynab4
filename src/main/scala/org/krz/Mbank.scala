package org.krz


import scala.io.Source

case class YnabColumn(
  date: String,
  category: String = "",
  payee: String,
  memo: String,
  inflow: BigDecimal,
  outflow: String = ""
) {

  def toCsv: Seq[String] = {
    def stringToCsv(s: String) = s""""$s""""
    def numberToCsv(b: BigDecimal) = b.toString()

    Seq(
      date,
      category,
      stringToCsv(payee),
      stringToCsv(memo),
      numberToCsv(inflow),
      outflow
    )
  }
}

object Mbank {

  val mbankEncoding = "Windows-1250"
  val mbankSeparator = ";"
  val mbankComma = ","

  val ynabSeparator = ","
  val ynabColumns = Seq("Date", "Category", "Payee", "Memo", "Inflow", "Outflow")

  def main(args: Array[String]): Unit = {
    val input = args.lift(0).getOrElse(sys.error("Please provide input file as first argument"))

    println(ynabColumns.mkString(ynabSeparator))
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
    YnabColumn(
      // TODO reformat date
      date = column(0),
      payee = column(4),
      memo = column(3),
      inflow = cleanupNumeric(column(6))
    )
  }

  def exportColumns = (column: YnabColumn) => column.toCsv.mkString(ynabSeparator)
}
