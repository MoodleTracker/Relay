@file:Suppress("RedundantModalityModifier")

package com.github.moodletracker.server

import com.github.moodletracker.tracking.upcoming.UpcomingScheduler
import io.quarkus.security.Authenticated
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import org.eclipse.microprofile.jwt.Claim
import org.eclipse.microprofile.jwt.Claims

@Path("/track")
@RequestScoped
open class TrackResource {

	@Inject
	private lateinit var upcomingScheduler: UpcomingScheduler

	@Inject
	@Claim(standard = Claims.sub)
	private lateinit var userIdString: String

	@Path("/upcoming")
	@POST
	@Authenticated
	open suspend fun trackUpcoming(): String {
		upcomingScheduler.schedule(userIdString.toInt())
		return "ok"
	}
}
