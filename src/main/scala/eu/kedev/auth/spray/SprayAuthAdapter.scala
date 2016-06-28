package eu.kedev.auth.spray

import javax.inject.Inject

import _root_.play.api.mvc.{Cookie, DiscardingCookie, Request}
import eu.kedev.auth._
import _root_.spray.routing.RequestContext
import _root_.spray.routing._
import _root_.spray.routing.Directives._
import _root_.spray.http._
import _root_.spray.http.HttpHeaders.RawHeader
import shapeless._

class SprayAuthAdapter @Inject()(protected val auth: HttpHeaderAuthentication) extends AuthAdapter[RequestContext, Route] {

  override protected def authRequest(request: RequestContext): AuthRequest = SprayAuthRequest(request)

  override protected def authResponse(response: Route): AuthResponse[Route] = SprayAuthResponse(response)

  private case class SprayAuthRequest(request: RequestContext) extends AuthRequest {

    override protected def cookieValue(name: String): Option[String] =
      request.request.cookies.find(c => c.name == name) map (_.value)

    override protected def headerValue(name: String): Option[String] =
      request.request.headers.find(h => h.name == name).map(_.value)
  }

  private case class SprayAuthResponse(response: Route) extends AuthResponse[Route] {

    override def withAuthCookie(token: AuthToken): AuthResponse[Route] = SprayAuthResponse {
      // domain, httponly, maxage, secure
      setCookie(HttpCookie(CookieName, token.encode, path = Some("/"))) { response }
    }

    override def get: Route = response

    override def withAuthHeader(token: AuthToken): AuthResponse[Route] = {
      SprayAuthResponse(
        respondWithHeader(RawHeader(CustomTokenHeader, token.encode))(response)
      )
    }

    override def withPersistentId(id: String): AuthResponse[Route] = {
      SprayAuthResponse(
        setCookie(HttpCookie(PersistentIdCookie, id, path = Some("/")))(response)
        // domain, httponly, maxage, secure
      )
    }

    override def discardingAuthCookie(): AuthResponse[Route] = {
      SprayAuthResponse(
        deleteCookie(HttpCookie(CookieName, "", path = Some("/")))(response)
        // domain, httponly, maxage, secure
      )
    }
  }

}
