package eu.kedev.auth

import javax.inject.Singleton

import eu.kedev.auth.servlet.ServletAuthAdapter
import net.codingwell.scalaguice.ScalaModule


class Module extends ScalaModule {

  override def configure(): Unit = {
    requireBinding(classOf[UserRepository])
    requireBinding(classOf[AuthTokenManager])

    bind[HttpHeaderAuthentication].in[Singleton]
  }
}
