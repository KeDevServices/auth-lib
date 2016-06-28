package eu.kedev.auth

import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


/**
  * @param secret We use HMAC-SHA1, so the key size can (and should) be up to 64 bytes.
  */
class HmacAuthTokenManager @Inject()(secret: Array[Byte]) extends AuthTokenManager {

  val secretAsString = new String(secret)

  def validate(token: AuthToken): Either[String, AuthToken] = {
    if (token.signature != sign(msgToSign(token.username, token.created))) {
      Left("wrong signature")
    } else {
      Right(token)
    }
  }

  def createTokenFor(username: String): AuthToken = {
    val created = Instant.now()
    AuthToken(username, created, sign(msgToSign(username, created)))
  }

  private def msgToSign(msg: String, date: Instant) = s"$msg::${date.toString}::$secretAsString"

  private def sign(message: String): String = {
     val mac = Mac.getInstance("HmacSHA1")
     mac.init(new SecretKeySpec(secret, "HmacSHA1"))
     val raw = mac.doFinal(message.getBytes("utf-8"))
     Base64.getEncoder.encodeToString(raw)
  }

}
