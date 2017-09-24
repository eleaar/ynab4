package org

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import java.time.ZonedDateTime

import scala.util.Try

package object krz {

  def cleanupDate(date: String, dateFormat: String): DateTime = DateTime.parse(date, DateTimeFormat.forPattern(dateFormat))

  def cleanupNumeric(s: String): BigDecimal = Try(
    BigDecimal(s.replace(",", ".").replaceAll("\\s", ""))
  ).getOrElse(sys.error(s"Could not parse numeric $s"))

  def cleanupQuotes(s: String): String = s.replace("\"", "").replaceAll("\\s\\s*", " ").trim

  def renderYnabRow: Ynab.Row => String = _.toCsv

}
