package dev.lordyorden.tradely.utilities

import com.google.firebase.firestore.SetOptions

class Constants {
    object DB {
        const val PRICES_REF = "prices"
        const val USERS_REF = "users"
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
            "bought")
    }

    object Activities{
        const val MAIN = "main"
        const val LOGIN = "login"
        const val STORAGE = "storage"
        const val FIRESTORE = "firestore"
    }
}