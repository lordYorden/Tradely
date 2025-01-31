package dev.lordyorden.tradely.interfaces

import dev.lordyorden.tradely.models.Profile

interface ProfileFetchCallback {
    fun onProfileFetch(profile: Profile)
    fun onProfileFetchFailed()
}