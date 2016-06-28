package eu.kedev.auth.vaadin

import javax.inject.Singleton

import eu.kedev.auth.{AuthTokenManager, HttpHeaderAuthentication, UserRepository}
import net.codingwell.scalaguice.ScalaModule

class Module extends ScalaModule {

  override def configure(): Unit = {
    requireBinding(classOf[UserRepository])
    requireBinding(classOf[AuthTokenManager])
    requireBinding(classOf[HttpHeaderAuthentication])

    bind[VaadinAuthAdapter].in[Singleton]
  }
}
