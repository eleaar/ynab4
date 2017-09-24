package org.krz

import org.joda.time.DateTime

object Ynab {

  private val ynabSeparator = ","
  private val ynabColumns = Seq("Date", "Category", "Payee", "Memo", "Inflow", "Outflow")
  private val dateFormat = "dd/mm/YYYY"

  val ynabHeader: String = ynabColumns.mkString(Ynab.ynabSeparator)

  case class Row(
    date: DateTime,
    category: String = "",
    payee: String,
    memo: String,
    inflow: BigDecimal,
    outflow: String = ""
  ) {

    def toCsv: String = {
      def stringToCsv(s: String) = s""""$s""""
      def numberToCsv(b: BigDecimal) = b.toString()

      Seq(
        date.toString(Ynab.dateFormat),
        category,
        stringToCsv(payee),
        stringToCsv(memo),
        numberToCsv(inflow),
        outflow
      ).mkString(Ynab.ynabSeparator)
    }
  }

}
