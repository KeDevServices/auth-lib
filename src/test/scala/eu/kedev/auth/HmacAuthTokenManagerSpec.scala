package eu.kedev.auth

import org.scalatest.{FreeSpec, MustMatchers}


class HmacAuthTokenManagerSpec extends FreeSpec with MustMatchers {

  "1" in {
    val tm = new HmacAuthTokenManager("password" getBytes "utf-8")

    val token = tm.createTokenFor("dagget")

    tm.validate(token) mustBe Right(token)
  }

}
