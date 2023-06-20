package com.github.moodletracker.client.service

import com.github.moodletracker.client.model.TokenServiceResponse
import io.quarkus.rest.client.reactive.ClientQueryParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "token-service")
@Path("/login")
@ClientQueryParam(name = "service", value = ["moodle_mobile_app"])
interface LoginService {
	@Path("/token.php")
	@GET
	suspend fun token(
		@QueryParam("username") username: String,
		@QueryParam("password") password: String
	): TokenServiceResponse
}
