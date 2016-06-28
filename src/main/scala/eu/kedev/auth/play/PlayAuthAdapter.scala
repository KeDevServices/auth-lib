package eu.kedev.auth.play

import java.util.UUID
import javax.inject.Inject

import eu.kedev.auth._
import _root_.play.api.mvc.{DiscardingCookie, Request, Result}
import _root_.play.api.mvc.Cookie

class PlayAuthAdapter @Inject() (protected val auth: HttpHeaderAuthentication) extends AuthAdapter[Request[_], Result] {

  override protected def authRequest(request: Request[_]): AuthRequest = PlayAuthRequest(request)

  override protected def authResponse(response: Result): AuthResponse[Result] = PlayAuthResponse(response)


  private case class PlayAuthRequest(request: Request[_]) extends AuthRequest {

    override protected def cookieValue(name: String): Option[String] =
      request.cookies.find(c => c.name == name) map (_.value)

    override protected def headerValue(name: String): Option[String] =
      request.headers.get(name)
  }

  private case class PlayAuthResponse(response: Result) extends AuthResponse[Result] {

    override def withAuthCookie(token: AuthToken): AuthResponse[Result] = {
      val cookie = Cookie(
        CookieName, token.encode
        // domain, httponly, maxage, secure
      )
      PlayAuthResponse(response.withCookies(cookie))
    }

    override def get: Result = response

    override def withAuthHeader(token: AuthToken): AuthResponse[Result] = {
      PlayAuthResponse(response.withHeaders((CustomTokenHeader, token.encode)))
    }

    override def withPersistentId(id: String): AuthResponse[Result] = {
      val cookie = Cookie(
        PersistentIdCookie, id
        // domain, httponly, maxage, secure
      )
      PlayAuthResponse(response.withCookies(cookie))
    }

    override def discardingAuthCookie(): AuthResponse[Result] = {
      val dc = DiscardingCookie(CookieName)
      PlayAuthResponse(response.discardingCookies(dc))
    }
  }

}
