package com.github.moodletracker.tracking.upcoming

import com.github.moodletracker.protocol.EventsUpcoming

interface UpcomingReceiver {
	fun onUpcoming(userId: Int, events: EventsUpcoming.UpcomingEvent)
}
