package domain

import java.time.format.DateTimeFormatter
import java.util.{Locale, MissingResourceException}

object Constants {
  val Formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
  val CountriesCodes: Array[Locale] = Locale.getAvailableLocales
  val DatabasePath: String = "/tmp/spark_output/movies"
}
