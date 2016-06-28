package eu.kedev.auth

import java.nio.charset.Charset
import java.time.Instant
import java.util.Base64

import _root_.play.api.libs.json.Json
import _root_.play.api.libs.json.Json._
import org.slf4j.LoggerFactory

import scala.util.Try

/**
  * An AuthToken can be used to authenticate against a service
  * @param username name of the client (human user or other service)
  * @param created timestamp of the creation of the token.
  * @param signature hash value based on username and created.
  */
case class AuthToken(
    username: String,
    created: Instant,
    signature: String) {

  /**
    * @return A base-64 encoded JSON string representing this auth token
    */
  def encode: String = {
    val json = AuthToken.format.writes(this).toString()
    Base64.getEncoder.encodeToString(json.getBytes(Charset.forName("UTF-8")))
  }

}

object AuthToken {

  implicit private val log = LoggerFactory.getLogger(classOf[AuthToken])

  implicit private val format = Json.format[AuthToken]

  /**
    * @param base64 A base-64 encoded JSON string representing an auth token
    * @return Some(AuthToken) or None
    */
  def decode(base64: String): Option[AuthToken] = {
    val json = TryAndLog.warn("illegal base64 string") {
      new String(Base64.getDecoder.decode(base64))
    }.toOption
    json map (x => AuthToken.format.reads(parse(x)).get)
  }

}
