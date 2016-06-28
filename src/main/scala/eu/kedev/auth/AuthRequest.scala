package eu.kedev.auth

import java.util.{Base64, UUID}

import org.slf4j.{Logger, LoggerFactory}

import scala.util.Try

private[auth] trait AuthRequest { self =>

  implicit protected val log = LoggerFactory.getLogger(self.getClass)

  def basic: Option[UserPass] =
    headerValue(BasicHeader) flatMap decodeBasicAuth

  def customUP: Option[UserPass] =
    headerValue(CustomUserPassHeader) flatMap decodeBasicAuth

  def customToken: Option[AuthToken] =
    headerValue(CustomTokenHeader) flatMap AuthToken.decode

  def cookie: Option[AuthToken] =
    cookieValue(CookieName) flatMap AuthToken.decode

  def persistentId: Option[UUID] =
    cookieValue(PersistentIdCookie) flatMap toUUID

  protected def cookieValue(name: String): Option[String]

  protected def headerValue(name: String): Option[String]

  private def toUUID(raw: String): Option[UUID] = {
    TryAndLog.warn("input is no uuid") {
      UUID.fromString(raw)
    }.toOption
  }

  private def decodeBasicAuth(maybeRaw: String): Option[UserPass] = {
    decode(maybeRaw.substring("Basic ".length)) flatMap { raw =>
      TryAndLog.warn("cannot extract user:pass") {
        val Array(u, p) = raw.split(":")
        UserPass(u, p)
      }.toOption
    }
  }

  private def decode(base64: String): Option[String] = {
    val decoded = TryAndLog.warn("illegal base64 string") {
      Base64.getDecoder.decode(base64)
    }
    decoded.toOption map (new String(_))
  }
}
