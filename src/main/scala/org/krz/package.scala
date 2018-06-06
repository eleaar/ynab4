package org

import scala.util.Try

package object krz {

  def notEmptyString(s: String): Option[String] = if(s.isEmpty) None else Some(s)

  def cleanupNumeric(s: String): BigDecimal = Try(
    BigDecimal(s.replace(",", ".").replaceAll("\\s", ""))
  ).getOrElse(sys.error(s"Could not parse numeric $s"))

  def cleanupQuotes(s: String): String = s.replace("\"", "").replaceAll("\\s\\s*", " ").trim

  def renderYnabRow: Ynab.Row => String = _.toCsv

}
