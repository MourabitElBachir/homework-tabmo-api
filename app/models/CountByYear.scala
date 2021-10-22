package models

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import play.api.libs.json.{Json, OFormat}

case class CountByYear(year: Short, moviesCount: Long)

object CountByYear {
  implicit val jsonMovieWrites: OFormat[CountByYear] = Json.format[CountByYear]
  implicit val encoderCountByYear: ExpressionEncoder[CountByYear] = org.apache.spark.sql.catalyst.encoders.ExpressionEncoder[CountByYear]
}

