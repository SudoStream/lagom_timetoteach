package io.sudostream.timetoteach.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import io.sudostream.timetoteach.api.TimetoteachService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class TimetoteachLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TimetoteachApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TimetoteachApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[TimetoteachService])
}

abstract class TimetoteachApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[TimetoteachService](wire[TimetoteachServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = TimetoteachSerializerRegistry

  // Register the timetoteach persistent entity
  persistentEntityRegistry.register(wire[TimetoteachEntity])
}
