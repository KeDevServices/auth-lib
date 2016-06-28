package eu.kedev.auth

import org.slf4j.Logger

import scala.util.Try

private[auth] object TryAndLog {

  def error[A](block: => A)(msg: String)(implicit log: Logger): Try[A] = {
    Try(block) recover {
      case e: Exception =>
        log.error(msg, e)
        throw e
    }
  }

  def warn[A](msg: String)(block: => A)(implicit log: Logger): Try[A] = {
    Try(block) recover {
      case e: Exception =>
        log.warn(msg, e)
        throw e
    }
  }

  def warn[A](block: => A)(msg: String)(implicit log: Logger): Try[A] = {
    Try(block) recover {
      case e: Exception =>
        log.warn(msg, e)
        throw e
    }
  }

  def info[A](block: => A)(msg: String)(implicit log: Logger): Try[A] = {
    Try(block) recover {
      case e: Exception =>
        log.info(msg, e)
        throw e
    }
  }

  def warn[A, B](either: => Either[B, A])(implicit log: Logger): Option[A] = {
    Try(either).recover {
      case e: Exception =>
        log.warn("unexpected exception", e)
        throw e
    }.toOption.flatMap {
      case Right(x) => Some(x)
      case Left(x)  =>
        log.warn(x.toString)
        None
    }
  }
}
