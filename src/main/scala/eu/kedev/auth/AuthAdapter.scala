package eu.kedev.auth

import java.util.UUID


/**
  * An AuthAdapter is used to work with http requests and responses
  * of a specific framework in an uniform way
  * @tparam A the request type
  * @tparam B the response type
  */
trait AuthAdapter[A, B] {

  /**
    * Tries to authenticate the request, either by finding an
    * valid auth token in it or by creating an auth token based
    * on the credentials found in the request.
    * @param request the request to be authenticated
    * @return Some(AuthToken) or None.
    */
  def authenticate(request: A): Option[AuthToken] =
    auth.authenticate(authRequest(request))

  /**
    * Adds an auth token cookie to the response.
    * @param token the AuthToken to be added
    * @param response the http response to which the cookie should be added
    * @return the response with the auth token set in a cookie
    */
  def setAuthCookie(token: AuthToken, response: B): B =
    authResponse(response).withAuthCookie(token).get

  /**
    * Remove an auth token cookie from the response
    * @param response the response from which the cookie should be deleted
    * @return the response with a "delete cookie" set
    */
  def removeAuthCookie(response: B): B =
    authResponse(response).discardingAuthCookie().get

  /**
    * Adds an auth token header to the response.
    * @param token the AuthToken to be added
    * @param response the http response to which the header should be added
    * @return the response with the auth token header set
    */
  def setCustomAuthHeader(token: AuthToken, response: B): B =
    authResponse(response).withAuthHeader(token).get

  /**
    * Will add a cookie with a random UUID to the response if none is set in the request
    * @param request the request to inspect for an existing cookie
    * @param response the response to which to add the cookie
    * @return the response with a uuid-cookie set (if none was present in the request)
    */
  def ensurePersistentIdCookie(request: A, response: B): B =
    if (authRequest(request).persistentId.isEmpty)
      authResponse(response).withPersistentId(UUID.randomUUID().toString).get
    else
      response

  /**
    * Retrieves the uuid from the id cookie.
    * @param request the request to inspect for an existing cookie
    * @return Some(UUID) or None
    */
  def getPersistentId(request: A): Option[UUID] =
    authRequest(request).persistentId

  protected def authRequest(request: A): AuthRequest

  protected def authResponse(response: B): AuthResponse[B]

  protected def auth: HttpHeaderAuthentication

}

