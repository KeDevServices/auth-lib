package eu.kedev


package object auth {

  val BasicHeader = "authorization"

  val CustomUserPassHeader = "vc-authorization"

  val CustomTokenHeader = "vc-auth-token"

  val CookieName = CustomTokenHeader

  val PersistentIdCookie = "vc-id"

}
