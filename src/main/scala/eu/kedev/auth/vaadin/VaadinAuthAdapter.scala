package eu.kedev.auth.vaadin

import java.util.UUID
import javax.inject.Inject
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}

import com.vaadin.server.{VaadinRequest, VaadinResponse}
import eu.kedev.auth._

class VaadinAuthAdapter @Inject()(protected val auth: HttpHeaderAuthentication)
    extends AuthAdapter[VaadinRequest, VaadinResponse] {

  override protected def authRequest(request: VaadinRequest): AuthRequest = VaadinAuthRequest(request)

  override protected def authResponse(response: VaadinResponse): AuthResponse[VaadinResponse] =
    VaadinAuthResponse(response)

  private case class VaadinAuthRequest(request: VaadinRequest) extends AuthRequest {

    override protected def cookieValue(name: String): Option[String] =
      Option(request.getCookies).getOrElse(Array.empty).find(c => c.getName == name) map (_.getValue)

    override protected def headerValue(name: String): Option[String] =
      Option(request.getHeader(name))
  }

  private case class VaadinAuthResponse(response: VaadinResponse) extends AuthResponse[VaadinResponse] {

    override def withAuthCookie(token: AuthToken): AuthResponse[VaadinResponse] = {
      val cookie = new Cookie(CookieName, token.encode)
      cookie.setPath("/")
      //cookie.setDomain()
      //cookie.setHttpOnly()
      //cookie.setMaxAge()
      //cookie.setSecure()
      response.addCookie(cookie)
      VaadinAuthResponse(response)
    }

    override def get: VaadinResponse = response

    override def withAuthHeader(token: AuthToken): AuthResponse[VaadinResponse] = {
      response.setHeader(CustomTokenHeader, token.encode)
      VaadinAuthResponse(response)
    }

    override def withPersistentId(id: String): AuthResponse[VaadinResponse] = {
      val cookie = new Cookie(PersistentIdCookie, id)
      cookie.setPath("/")
      //cookie.setDomain()
      //cookie.setHttpOnly()
      //cookie.setMaxAge()
      //cookie.setSecure()
      response.addCookie(cookie)
      VaadinAuthResponse(response)
    }

    override def discardingAuthCookie(): AuthResponse[VaadinResponse] = {
      val cookie = new Cookie(CookieName, null)
      cookie.setPath("/")
      //cookie.setDomain()
      //cookie.setHttpOnly()
      cookie.setMaxAge(-86400)
      //cookie.setSecure()
      response.addCookie(cookie)
      VaadinAuthResponse(response)
    }
  }

}
