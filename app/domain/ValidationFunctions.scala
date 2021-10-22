package domain

import domain.Constants.{CountriesCodes, Formatter}

import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.MissingResourceException

object ValidationFunctions {

  // Country ISO alpha-3 validation
  val validateCountry: String => Boolean =
    country => {
      CountriesCodes.map { cc =>
        try {
          cc.getISO3Country
        } catch {
          case _: MissingResourceException => ""
        }
      }
        .filter(_.nonEmpty).contains(country)
    }


  // Custom French release validation
  val validateDate: Option[String] => Boolean =
  {
    case Some(dateStr) => {
      try {
        LocalDate.parse(dateStr, Formatter)
        true
      } catch {
        case _: Throwable => false
      }
    }
    case None => true
  }


  // Custom Genre validation
  val validateGenre: List[String] => Boolean =
    list => {
      list.nonEmpty && list.forall(_.length <= 50)
    }


  // Custom original Path validation
  val validateOriginalTitleFilter: Option[String] => Boolean =
  {
    case Some(_) => true
    case None => false
  }


  // Custom FRA genre validation
  val validateFrenchFilter: String => Boolean =
    country => {
      country == "FRA"
    }

}
