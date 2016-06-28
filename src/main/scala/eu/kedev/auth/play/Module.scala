package eu.kedev.auth.play

import javax.inject.Singleton

import eu.kedev.auth.{AuthAdapter, AuthTokenManager, HttpHeaderAuthentication, UserRepository}
import net.codingwell.scalaguice.ScalaModule

class Module extends ScalaModule {

  override def configure(): Unit = {
    requireBinding(classOf[UserRepository])
    requireBinding(classOf[AuthTokenManager])
    requireBinding(classOf[HttpHeaderAuthentication])

    bind[PlayAuthAdapter].in[Singleton]
  }
}
