package dev.lordyorden.tradely.models

import android.util.Log
import java.util.Locale

data class Profile private constructor(
    var id: String,
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
        id = "",
        profilePic = "",
        name = "",
        netWorth = 0.0,
        country = "",
        description = "",
        change = 0.0,
        followers = mutableListOf(),
        following = mutableListOf()
    )

    companion object CountryCodeCheck {
        fun isValidCountry(countryCode: String): Boolean {
            try {
                Locale.getISOCountries().first { it == countryCode }
            }catch (_: NoSuchElementException){
                return false
            }
            return countryCode.length <= 2
        }
    }


    class Builder {
        private var id: String = ""
        private var profilePic: String = ""
        private var name: String = ""
        private var netWorth: Double = 0.0
        private var country: String = "AQ"
        private var description: String = ""
        private var change: Double = 0.0
        private var followers: MutableList<String> = mutableListOf()
        private var following: MutableList<String> = mutableListOf()


        fun profilePic(profilePic: String) = apply { this.profilePic = profilePic }
        fun name(name: String) = apply { this.name = name }
        fun netWorth(netWorth: Double) = apply { this.netWorth = netWorth }
        fun country(country: String) = apply {
            if(isValidCountry(country))
                this.country = country
            else
                Log.e("Profile creation", "$country is not a valid country code")
        }

        fun description(description: String) = apply { this.description = description }
        fun change(change: Double) = apply { this.change = change }
        fun id(id: String) = apply { this.id = id }
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
