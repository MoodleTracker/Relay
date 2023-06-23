package com.github.moodletracker.client.service

import com.github.moodletracker.protocol.EventsUpcoming
import io.quarkus.rest.client.reactive.ClientQueryParam
import io.smallrye.mutiny.Uni
import jakarta.json.JsonObject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "web-service")
@Path("/webservice/rest/server.php")
@ClientQueryParam(name = "moodlewsrestformat", value = ["json"])
interface WebService {
	@GET
	@ClientQueryParam(name = "wsfunction", value = ["core_webservice_get_site_info"])
	suspend fun getSiteInfo(@QueryParam("wstoken") token: String): JsonObject

	class UpcomingEventsResponse {
		lateinit var events: Collection<EventsUpcoming.UpcomingEvent>

		override fun toString(): String {
			return "UpcomingEventsResponse(events=$events)"
		}
	}

	@GET
	@ClientQueryParam(name = "wsfunction", value = ["core_calendar_get_calendar_upcoming_view"])
	fun getUpcomingEvents(@QueryParam("wstoken") token: String): Uni<UpcomingEventsResponse>
}
