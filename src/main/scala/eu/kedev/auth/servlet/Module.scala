package eu.kedev.auth.servlet

import eu.kedev.auth.{AuthAdapter, AuthTokenManager, HttpHeaderAuthentication, UserRepository}
import net.codingwell.scalaguice.ScalaModule
import javax.inject.Singleton

class Module extends ScalaModule {

  override def configure(): Unit = {
    requireBinding(classOf[UserRepository])
    requireBinding(classOf[AuthTokenManager])
    requireBinding(classOf[HttpHeaderAuthentication])

    bind[ServletAuthAdapter].in[Singleton]
  }
}
