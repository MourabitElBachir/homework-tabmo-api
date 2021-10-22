package domain

import play.api.libs.json.JsonValidationError

object ValidationErrors {

  val CountryAlphaCodeUnavailableError: JsonValidationError =
    JsonValidationError("Country ISO alpha-3 code incorrect")

  val GenreValidationError: JsonValidationError =
    JsonValidationError("Invalid genre given : At least one element with 50 max characters")

  val OriginalTitleError: JsonValidationError =
    JsonValidationError("No original title given")

  val FrenchFilterValidationError: JsonValidationError =
    JsonValidationError("No FRA country given")

  val NonFrenchFilterValidationError: JsonValidationError =
    JsonValidationError("non FRA country without original title")
}
