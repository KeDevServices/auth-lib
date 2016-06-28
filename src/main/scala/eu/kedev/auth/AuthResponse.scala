package eu.kedev.auth

private[auth] trait AuthResponse[A] {

  def withAuthCookie(token: AuthToken): AuthResponse[A]

  def discardingAuthCookie(): AuthResponse[A]

  def withAuthHeader(token: AuthToken): AuthResponse[A]

  def withPersistentId(id: String): AuthResponse[A]

  def get: A

}
