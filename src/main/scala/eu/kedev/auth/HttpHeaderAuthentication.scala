package eu.kedev.auth

import java.util.Base64
import javax.inject.Inject

/**
  * Used by an AuthAdapter to do authentication.
  * @param users a user repository to check credentials
  * @param tokenManager the AuthTokenManager to use
  */
class HttpHeaderAuthentication @Inject() (
      users: UserRepository,
      tokenManager: AuthTokenManager) {

  /**
    * Authenticates a request by either using the AuthTokenManager or the
    * UserRepository
    * @param h the request
    * @return Some(AuthToken) if valid authentication information was found or None
    */
  def authenticate(h: AuthRequest): Option[AuthToken] = {
    (h.basic, h.customUP, h.customToken, h.cookie) match {
      case (Some(b), None, None, None) => doBasic(b)
      case (None, Some(c), None, None) => doCustomUP(c)
      case (None, None, Some(c), None) => doCustomToken(c)
      case (None, None, None, Some(c)) => doCookie(c)
      case (None, None, None, None)    => None
      case _ => None
    }
  }

  private def doCustomUP(c: UserPass) = doBasic(c)

  private def doBasic(b: UserPass) = {
    if (users authenticate (b.user, b.pass))
      Some(tokenManager createTokenFor b.user)
    else
      None
  }

  private def doCookie(c: AuthToken) = doCustomToken(c)

  private def doCustomToken(token: AuthToken) = {
    tokenManager validate token match {
      case Left(str)=> None
      case Right(t) => Some(t)
    }
  }
}
