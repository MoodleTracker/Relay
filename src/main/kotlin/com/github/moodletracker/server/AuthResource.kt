package com.github.moodletracker.server

import com.github.moodletracker.client.service.LoginService
import com.github.moodletracker.client.service.WebService
import com.github.moodletracker.model.UserToken
import com.github.moodletracker.repository.UserTokenRepository
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.Duration

@Path("/auth")
class AuthResource {
	@Inject
	lateinit var userTokenRepository: UserTokenRepository

	@RestClient
	lateinit var loginService: LoginService

	@RestClient
	lateinit var webService: WebService

	@Path("/basic")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	suspend fun loginByBasicAuth(
		@HeaderParam("X-Username") username: String,
		@HeaderParam("X-Password") password: String
	): String {
		val tokenResponse = loginService.token(username, password)
		return login(tokenResponse.token)
	}


	@Path("/token")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	suspend fun loginByToken(@HeaderParam("X-Token") token: String): String {
		return login(token)
	}

	suspend fun login(token: String): String {
		val userToken = UserToken()
		userToken.userId = webService.getSiteInfo(token).getInt("userid")
		userToken.moodleToken = token

		val userId = userTokenRepository.insert(userToken).map { it.userId.toString() }.awaitSuspending()
		return Jwt.subject(userId)
			.expiresIn(Duration.ofDays(7))
			.sign()
	}
}
