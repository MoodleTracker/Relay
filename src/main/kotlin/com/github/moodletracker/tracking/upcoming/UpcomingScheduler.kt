package com.github.moodletracker.tracking.upcoming

import com.github.moodletracker.client.service.WebService
import com.github.moodletracker.repository.UserTokenRepository
import io.quarkus.scheduler.Scheduler
import io.smallrye.mutiny.Multi
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class UpcomingScheduler {
    @Inject
    private lateinit var userTokenRepository: UserTokenRepository

    @RestClient
    private lateinit var moodleService: WebService

    @Inject
    private lateinit var scheduler: Scheduler

    @Inject
    private lateinit var upcomingReceiver: UpcomingReceiver

    suspend fun schedule(userId: Int) {
        if (scheduler.getScheduledJob("upcoming-$userId") != null) {
            return
        }

        scheduler
            .newJob("upcoming-$userId")
            .setInterval("1m")
            .setAsyncTask { _ ->
                userTokenRepository.findById(userId)
                    .onItem().transformToUni { userToken -> moodleService.getUpcomingEvents(userToken!!.moodleToken) }
                    .onItem().transformToMulti { Multi.createFrom().items(it.events.stream()) }
                    .invoke { events -> upcomingReceiver.onUpcoming(userId, events) }
                    .toUni()
                    .replaceWithVoid()
            }.schedule()
    }
}
