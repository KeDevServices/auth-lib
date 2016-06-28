package eu.kedev.auth.servlet

import java.util.UUID
import javax.inject.Inject
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}

import eu.kedev.auth._

class ServletAuthAdapter @Inject() (protected val auth: HttpHeaderAuthentication)
    extends AuthAdapter[HttpServletRequest, HttpServletResponse] {

  override protected def authRequest(request: HttpServletRequest): AuthRequest = ServletAuthRequest(request)

  override protected def authResponse(response: HttpServletResponse): AuthResponse[HttpServletResponse] =
    ServletAuthResponse(response)

  private case class ServletAuthRequest(request: HttpServletRequest) extends AuthRequest {

    override protected def cookieValue(name: String): Option[String] =
      Option(request.getCookies).getOrElse(Array.empty).find(c => c.getName == name) map (_.getValue)

    override protected def headerValue(name: String): Option[String] =
      Option(request.getHeader(name))
  }

  private case class ServletAuthResponse(response: HttpServletResponse) extends AuthResponse[HttpServletResponse] {

    override def withAuthCookie(token: AuthToken): AuthResponse[HttpServletResponse] = {
      val cookie = new Cookie(CookieName, token.encode)
      cookie.setPath("/")
      //cookie.setDomain()
      //cookie.setHttpOnly()
      //cookie.setMaxAge()
      //cookie.setSecure()
      response.addCookie(cookie)
      ServletAuthResponse(response)
    }

    override def get: HttpServletResponse = response

    override def withAuthHeader(token: AuthToken): AuthResponse[HttpServletResponse] = {
      response.setHeader(CustomTokenHeader, token.encode)
      ServletAuthResponse(response)
    }

    override def withPersistentId(id: String): AuthResponse[HttpServletResponse] = {
      val cookie = new Cookie(PersistentIdCookie, id)
      cookie.setPath("/")
      //cookie.setDomain()
      //cookie.setHttpOnly()
      //cookie.setMaxAge()
      //cookie.setSecure()
      response.addCookie(cookie)
      ServletAuthResponse(response)
    }

    override def discardingAuthCookie(): AuthResponse[HttpServletResponse] = {
      val cookie = new Cookie(CookieName, null)
      cookie.setPath("/")
      //cookie.setDomain()
      //cookie.setHttpOnly()
      cookie.setMaxAge(-86400)
      //cookie.setSecure()
      response.addCookie(cookie)
      ServletAuthResponse(response)
    }
  }
}
