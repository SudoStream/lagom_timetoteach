package io.sudostream.timetoteachstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import io.sudostream.timetoteachstream.api.TimetoteachStreamService
import io.sudostream.timetoteach.api.TimetoteachService

import scala.concurrent.Future

/**
  * Implementation of the TimetoteachStreamService.
  */
class TimetoteachStreamServiceImpl(timetoteachService: TimetoteachService) extends TimetoteachStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(timetoteachService.hello(_).invoke()))
  }
}
