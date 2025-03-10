package dev.lordyorden.tradely.utilities

import com.google.firebase.firestore.SetOptions

class Constants {
    object DB {
        const val PRICES_REF = "prices"
        const val STOCKS_REF = "stocks"
        const val PROFILES_REF = "profiles"

        val PROFILE_UPLOAD_DEFAULT_OPTIONS = SetOptions.mergeFields(
            "profilePic",
            "name",
            "netWorth",
            "country",
            "description",
            "change",
            "followers",
            "following",
            "bought",
            "watchlist")

        val PROFILE_UPLOAD_EDITABLE_OPTIONS = SetOptions.mergeFields(
            "name",
            "description"
        )
    }
}