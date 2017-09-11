package io.sudostream.timetoteach.impl

import io.sudostream.timetoteach.api
import io.sudostream.timetoteach.api.{TimetoteachService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the TimetoteachService.
  */
class TimetoteachServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends TimetoteachService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the timetoteach entity for the given ID.
    val ref = persistentEntityRegistry.refFor[TimetoteachEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the timetoteach entity for the given ID.
    val ref = persistentEntityRegistry.refFor[TimetoteachEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(TimetoteachEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[TimetoteachEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
