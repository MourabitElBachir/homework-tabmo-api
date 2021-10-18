package controllers

import models.Movie
import play.api.libs.json.{Json, OFormat}

import javax.inject._
import play.api.mvc._

import scala.collection.mutable

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val movieList = new mutable.ListBuffer[Movie]()
  movieList += Movie("test", "France", 2006, "", "", "", List(""), 1)
  movieList += Movie("some other value", "Italy", 2008, "", "", "", List(""), 2)

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  implicit val movieFormatter: OFormat[Movie] = Json.format[Movie]

  def addNewMovie(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val movieOpt: Option[Movie] =
      jsonObject.flatMap(
        Json.fromJson[Movie](_).asOpt
      )

    movieOpt match {
      case Some(movie) =>
        movieList += movie
        Created(Json.toJson(movie))
      case None =>
        BadRequest
    }
  }
}
