package dev.lordyorden.tradely.interfaces.profile

import dev.lordyorden.tradely.models.Profile

interface ProfileFetchCallback {
    fun onProfileFetch(profile: Profile)
    fun onProfileFetchFailed()
    fun onFetchComplete() = Unit
}