package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json.toJson
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class MovieControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "MovieController GET[list request]" should {

    "render the index page from a new instance of controller" in {
      val controller = new MovieController(stubControllerComponents())
      val home = controller.list().apply(FakeRequest(GET, "/list"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the application" in {
      val controller = inject[MovieController]
      val home = controller.list().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/list")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }

  "MovieController GET[countByYear request]" should {

    "render the index page from a new instance of controller" in {
      val controller = new MovieController(stubControllerComponents())
      val home = controller.countByYear().apply(FakeRequest(GET, "/compute"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the application" in {
      val controller = inject[MovieController]
      val home = controller.countByYear().apply(FakeRequest(GET, "/compute"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/compute")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }

  "MovieController GET[SearchByGenre request]" should {

    "render the index page from a new instance of controller" in {
      val controller = new MovieController(stubControllerComponents())
      val home = controller.countByYear().apply(FakeRequest(GET, "/movies/comedy"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the application" in {
      val controller = inject[MovieController]
      val home = controller.countByYear().apply(FakeRequest(GET, "/movies/comedy"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/movies/comedy")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }
  "MovieController GET[AddMovie request]" should {
    "respond to POST JSON with description field present" in {
      val request = FakeRequest(POST, "/add")
        .withJsonBody(
          toJson(
            Map(
              "title"          -> toJson("Fight Club"),
              "country"        -> toJson("USA"),
              "year"           -> toJson(1999),
              "original_title" -> toJson("Fight Club"),
              "french_release" -> toJson("1999/11/10"),
              "synopsis"       -> toJson(""),
              "genre"          -> toJson(Seq("Thriller", "Drame")),
              "ranking"        -> toJson(9)
            )
          )
        )
      val home = route(app, request).get
      status(home) mustBe OK
    }
  }

}
