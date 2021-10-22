package domain

import domain.ValidationFunctions.{validateCountry, validateDate, validateFrenchFilter, validateOriginalTitleFilter}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting
import utils.Utils

class ValidationFunctionsSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with Utils {
   "validateCountry" should {
    "return true" in {
      validateCountry("USA") mustBe true
    }

    "return false" in {
      validateCountry("US") mustBe false
    }
  }
  "validateDate" should {
    "return true" in {
      validateDate(Some("1999/11/10")) mustBe true
    }

    "return false" in {
      validateDate(Some("1999/10")) mustBe false
    }
  }
  "validateOriginalTitleFilter" should {
    "return true" in {
      validateOriginalTitleFilter(Some("Fight Club")) mustBe true
    }

    "return false" in {
      validateOriginalTitleFilter(None) mustBe false
    }
  }

  "validateFrenchFilter" should {
    "return true" in {
      validateFrenchFilter("FRA") mustBe true
    }
    "return false" in {
      validateFrenchFilter("USA") mustBe false
    }
  }
}
