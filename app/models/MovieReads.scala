package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import domain.ValidationFunctions._
import domain.ValidationErrors._

case class MovieReads(
                  title: String,
                  country: String,
                  year: Short,
                  original_title: Option[String] = None,
                  french_release: Option[String] = None,
                  synopsis: Option[String] = None,
                  genre: Seq[String],
                  ranking: Short
                )


object MovieReads {

  // Will be used in country Path :
  val originalPath: Reads[Option[String]] = (JsPath \ "original_title").readNullable[String](maxLength[String](250))

  // used in both french & external movies
  val countryPath: Reads[String] = (JsPath \ "country").read[String](minLength[String](3).keepAnd(maxLength[String](3)))

  // French movie path
  val frenchMoviePath: Reads[String] = countryPath
    .filter(FrenchFilterValidationError)(validateFrenchFilter)

  // External movie path
  val externalMoviePath: Reads[String] = countryPath
    .filter(CountryAlphaCodeUnavailableError)(validateCountry)
    .filter(NonFrenchFilterValidationError)(!validateFrenchFilter(_))
    .keepAnd(originalPath.filter(OriginalTitleError)(validateOriginalTitleFilter))

  // Movie reads for verification
  implicit val movieReads: Reads[MovieReads] =
    (
      (JsPath \ "title").read[String](minLength[String](1).keepAnd(maxLength[String](250))) and
        (
          frenchMoviePath
            or
            externalMoviePath
          )
           and
        (JsPath \ "year").read[Short] and
        originalPath and
        (JsPath \ "french_release").readNullable[String].filter(JsonValidationError("Invalid given french release date format"))(
          validateDate
        ) and
        (JsPath \ "synopsis").readNullable[String] and
        (JsPath \ "genre").read[List[String]].filter(GenreValidationError)(validateGenre) and
        (JsPath \ "ranking").read[Short](min[Short](1).keepAnd(max[Short](10)))
      )(MovieReads.apply _)
}