package dev.lordyorden.tradely.utilities

class Constants {
    object DB {
        const val MESSAGE_REF = "message"
        const val MOVIE_REF = "movies"
        const val USERS_REF = "users"
        const val PROFILES_REF = "profiles"
    }
    object Data {
        const val ACTORS_MIN_LINES: Int = 1
        const val OVERVIEW_MIN_LINES: Int = 3
    }

    object Activities{
        const val MAIN = "main"
        const val MOVIE_LIST = "movie_list"
        const val LOGIN = "login"
        const val STORAGE = "storage"
        const val FIRESTORE = "firestore"
    }
}