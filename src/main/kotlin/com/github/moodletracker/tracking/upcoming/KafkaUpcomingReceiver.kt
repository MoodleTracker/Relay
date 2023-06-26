package com.github.moodletracker.tracking.upcoming

import com.github.moodletracker.protocol.EventsUpcoming.UpcomingEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

@ApplicationScoped
class KafkaUpcomingReceiver : UpcomingReceiver {
	@Inject
	@Channel("upcoming-out")
	private lateinit var channel: Emitter<UpcomingEvent>

	override fun onUpcoming(userId: Int, events: UpcomingEvent) {
		channel.send(events)
	}
}
