package dev.lordyorden.tradely.models

import java.util.UUID

data class Profile private constructor(
    var id: UUID,
    val profilePic: String,
    val name: String,
    val netWorth: Double,
    val country: String,
    val description: String,
    val change: Double,
    var followers: MutableList<String>,
    var following: MutableList<String>,
) {

    constructor(): this(
        id = UUID.randomUUID(),
        profilePic = "",
        name = "",
        netWorth = 0.0,
        country = "",
        description = "",
        change = 0.0,
        followers = mutableListOf(),
        following = mutableListOf()
    )


    class Builder {
        private var id: UUID = UUID.randomUUID()
        private var profilePic: String = ""
        private var name: String = ""
        private var netWorth: Double = 0.0
        private var country: String = ""
        private var description: String = ""
        private var change: Double = 0.0
        private var followers: MutableList<String> = mutableListOf()
        private var following: MutableList<String> = mutableListOf()


        fun profilePic(profilePic: String) = apply { this.profilePic = profilePic }
        fun name(name: String) = apply { this.name = name }
        fun netWorth(netWorth: Double) = apply { this.netWorth = netWorth }
        fun country(country: String) = apply { this.country = country }
        fun description(description: String) = apply { this.description = description }
        fun change(change: Double) = apply { this.change = change }
        fun id(id: UUID) = apply { this.id = id }
        fun followers(followers: MutableList<String>) = apply { this.followers = followers }
        fun following(following: MutableList<String>) = apply { this.following = following }

        fun build() = Profile(
            id = id,
            profilePic = profilePic,
            name = name,
            netWorth = netWorth,
            country = country,
            description = description,
            change = change,
            followers = followers,
            following = following
        )
    }
}
