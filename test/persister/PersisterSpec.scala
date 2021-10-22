package persister

import controllers.MovieController
import domain.Constants.Formatter
import models.MovieWrites
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json.toJson
import play.api.test.{FakeRequest, Injecting}
import play.api.test.Helpers.{GET, contentType, status, stubControllerComponents}
import utils.Utils

import java.time.LocalDate
import scala.util.Success

object PersisterSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with Utils {
  "Persister" should {
    "persist a movieWriter with Success" in {
      Persister
        .persist(
          MovieWrites(
            "Fight Club",
            "USA",
            1999,
            "Fight Club",
            LocalDate.parse("1999/11/10", Formatter),
            "",
            Seq("Thriller", "Drame"),
            9
          ), "resources/saving") mustBe a[Success[Unit]]
    }
  }
}
