package com.github.moodletracker.server

import com.github.moodletracker.client.service.LoginService
import com.github.moodletracker.client.service.WebService
import com.github.moodletracker.model.UserToken
import com.github.moodletracker.repository.UserTokenRepository
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RestClient

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
	suspend fun loginByBasicAuth(@HeaderParam("X-Username") username: String, @HeaderParam("X-Password") password: String): String {
		val tokenResponse = loginService.token(username, password)
		val userToken = UserToken()
		userToken.userId = webService.getSiteInfo(tokenResponse.token).getInt("userid")
		userToken.moodleToken = tokenResponse.token

		return userTokenRepository.insert(userToken).map { it.userId.toString() }.awaitSuspending()
	}


	@Path("/token")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	suspend fun loginByToken(@HeaderParam("X-Token") token: String): String {
		val userToken = UserToken()
		userToken.userId = webService.getSiteInfo(token).getInt("userid")
		userToken.moodleToken = token

		return userTokenRepository.insert(userToken).map { it.userId.toString() }.awaitSuspending()
	}
}
