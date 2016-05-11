package org.krz

object Ynab {

  val ynabSeparator = ","
  val ynabColumns = Seq("Date", "Category", "Payee", "Memo", "Inflow", "Outflow")
  val dateFormat = "dd/mm/YYYY"

  case class Column(
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

}
