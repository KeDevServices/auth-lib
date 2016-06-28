package eu.kedev.auth

/**
  * Interface for handling AuthTokens
  */
trait AuthTokenManager {

  /**
    * Validate an auth token.
    * @param token the token to be validated
    * @return Right(AuthToken) if it is a valid token, or some Left(String) containing an error message.
    */
  def validate(token: AuthToken): Either[String, AuthToken]

  /**
    * @param username the user for whom to create a token
    * @return a new AuthToken for the given username
    */
  def createTokenFor(username: String): AuthToken
}
