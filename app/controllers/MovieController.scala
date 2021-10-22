package controllers

import domain.Constants.DatabasePath
import models.{CountByYear, MovieReads, MovieWrites}
import org.apache.spark.sql.functions.{array_contains, col, count}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.SparkSession
import persister.Persister
import play.api.libs.json.{JsError, JsSuccess, Json}

import javax.inject._
import play.api.mvc._
import reader.Reader

import scala.util.{Failure, Success}

@Singleton
class MovieController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  import org.apache.spark.sql.catalyst.ScalaReflection
  val schema: StructType = ScalaReflection.schemaFor[MovieWrites].dataType.asInstanceOf[StructType]

  implicit val spark = SparkSession
    .builder()
    .enableHiveSupport()
    .config("spark.master", "local")
    .appName("Spark Hive Example")
    .enableHiveSupport()
    .getOrCreate()

  def addNewMovie(): Action[AnyContent] = Action { implicit request =>

    val bodyAsJson = request.body.asJson.get

    bodyAsJson.validate[MovieReads] match {

      case success: JsSuccess[MovieReads] =>
        val movieReads = success.value
        val movieWrites = MovieWrites
          .buildFrom(movieReads)

        Persister.persist(movieWrites, DatabasePath) match {
          case Success(exec) => Ok("Validation passed! Movie is saved : " + movieWrites)
          case Failure(e) => BadRequest(s"Error in saving new movie - ${e.getMessage}")
        }
        // Persist new movie in hive table

      case JsError(errors) =>

        BadRequest(
          "Validation failed : <br>" +
          errors.map {
            case (path, validationErrors) =>
              s"${ if(path.toString.length >= 2) path.toString.substring(1).capitalize else path.toString} -> ${
                validationErrors.flatMap { elm => elm.messages.map(_.split(s"\\.").last.capitalize + " error") }
                  .mkString("<br>")}"
          }.mkString("<br>")
        )
    }
  }

  // 1.2 : first part
  def SearchByGenre(genre: String): Action[AnyContent] = Action { implicit request =>
    // Result in Json Format
    Ok(Json.toJson(Reader
      .read(DatabasePath, schema)
      .filter(array_contains(col("genre"), genre))
      .orderBy(col("year"), col("title"))
      .as[MovieWrites]
      .collect()
    ))
  }

  // 1.2 : second part
  def list: Action[AnyContent] = Action { implicit request =>
    // Result in Json Format
    val result = Reader
      .read(DatabasePath, schema)
      .as[MovieWrites]
      .collect
      .toList
    Ok(Json.toJson(result))
  }

  // 1.3 :
  def countByYear: Action[AnyContent] = Action { implicit request =>
    // Result in Json Format
    val result = Reader
      .read(DatabasePath, schema)
      .groupBy(col("year"))
      .agg(count(col("title")).as("moviesCount"))
      .as[CountByYear]
      .collect
      .toList
    Ok(Json.toJson(result))
  }


}
