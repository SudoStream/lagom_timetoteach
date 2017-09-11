package io.sudostream.timetoteachstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The timetoteach stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the TimetoteachStream service.
  */
trait TimetoteachStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("timetoteach-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

