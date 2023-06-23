package com.github.moodletracker.repository

import com.github.moodletracker.model.UserToken
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.hibernate.reactive.mutiny.Mutiny

@ApplicationScoped
class UserTokenRepository : PanacheRepository<UserToken> {

    @Inject
    private lateinit var sf: Mutiny.SessionFactory

    fun findById(id: Int): Uni<UserToken> = sf.withTransaction { session -> session.find(UserToken::class.java, id) }

    suspend fun insert(userToken: UserToken): Uni<UserToken> = sf.withTransaction { session ->
        session.find(UserToken::class.java, userToken.userId!!)
            .onItem().ifNotNull().call { user ->
                user.moodleToken = userToken.moodleToken
                session.merge(user)
            }
            .onItem().ifNull().switchTo { session.persist(userToken).map { userToken } }
    }
}
