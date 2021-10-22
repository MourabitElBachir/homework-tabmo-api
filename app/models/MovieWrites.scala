package models

import domain.Constants.Formatter
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

case class MovieWrites(
                       title: String,
                       country: String,
                       year: Short,
                       original_title: String,
                       french_release: LocalDate,
                       synopsis: String,
                       genre: Seq[String],
                       ranking: Short
                     )


object MovieWrites {
  implicit val jsonMovieWrites: OFormat[MovieWrites] = Json.format[MovieWrites]
  implicit val encoder: ExpressionEncoder[MovieWrites] = org.apache.spark.sql.catalyst.encoders.ExpressionEncoder[MovieWrites]
  def buildFrom(movieReads: MovieReads) : MovieWrites = {
    MovieWrites(
      movieReads.title,
      movieReads.country,
      movieReads.year,
      movieReads.original_title.orNull,
      movieReads.french_release.map(LocalDate.parse(_, Formatter)).orNull,
      movieReads.synopsis.orNull,
      movieReads.genre.toSeq,
      movieReads.ranking
    )
  }
}
