package org.krz

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Ynab {

  private val ynabSeparator = ","
  private val ynabColumns = Seq("Date", "Payee", "Category",  "Memo", "Outflow", "Inflow")
  private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

  val ynabHeader: String = ynabColumns.mkString(Ynab.ynabSeparator)

  case class Row(
    date: LocalDate,
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
        date.format(dateFormat),
        stringToCsv(payee),
        category,
        stringToCsv(memo),
        outflow,
        numberToCsv(inflow)
      ).mkString(Ynab.ynabSeparator)
    }
  }

}
