package com.github.moodletracker.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UserToken {
    @Id
    var userId: Int? = null
    lateinit var moodleToken: String
}
