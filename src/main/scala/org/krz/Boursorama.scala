package org.krz

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.io.Source

object Boursorama {

  private case class Row(
    dateOperation: LocalDate,
    dateValeur: LocalDate,
    libelle: String,
    categorie: String,
    categoryParent: String,
    supplierFound: String,
    amount: BigDecimal,
    accountNum: String,
    accountLabel: String,
    accountBalance: BigDecimal
  )

  private val encoding = "Windows-1250"
  private val separator = ";"
  private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

  def main(args: Array[String]): Unit = {
    val input = args.headOption.getOrElse(sys.error("Please provide input file as first argument"))

    println(Ynab.ynabHeader)
    Source.fromFile(input, encoding).getLines()
      .drop(1) // headers
      .takeWhile(_.nonEmpty) // discard last few lines
      .foreach(importColumns andThen reorganiseColumns andThen renderYnabRow andThen println)
  }

  private def importColumns = (data: String) => data.split(separator).toSeq.map(cleanupQuotes) match {
    case Seq(dateOperation, dateValeur, libelle, categorie, categoryParent, supplierFound, amount, accountNum, accountLabel, accountBalance) =>
      Row(
        dateOperation = LocalDate.parse(dateOperation, dateFormat),
        dateValeur = LocalDate.parse(dateValeur, dateFormat),
        libelle = libelle,
        categorie = categorie,
        categoryParent = categoryParent,
        supplierFound = supplierFound,
        amount = cleanupNumeric(amount),
        accountNum = accountNum,
        accountLabel = accountLabel,
        accountBalance = cleanupNumeric(accountBalance)
      )
    case x => sys.error(s"unexpected column format $x")
  }

  private def reorganiseColumns = (row: Row) => Ynab.Row(
    date = row.dateOperation,
    payee = row.supplierFound,
    memo = row.libelle,
    inflow = row.amount
  )

}
