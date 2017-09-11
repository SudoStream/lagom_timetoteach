package io.sudostream.timetoteachstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import io.sudostream.timetoteachstream.api.TimetoteachStreamService
import io.sudostream.timetoteach.api.TimetoteachService
import com.softwaremill.macwire._

class TimetoteachStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TimetoteachStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TimetoteachStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[TimetoteachStreamService])
}

abstract class TimetoteachStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[TimetoteachStreamService](wire[TimetoteachStreamServiceImpl])

  // Bind the TimetoteachService client
  lazy val timetoteachService = serviceClient.implement[TimetoteachService]
}
