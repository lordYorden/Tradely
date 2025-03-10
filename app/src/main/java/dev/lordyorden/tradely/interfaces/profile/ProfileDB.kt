package dev.lordyorden.tradely.interfaces.profile

import dev.lordyorden.tradely.models.Profile

interface ProfileDB {

    fun updateProfile(newProfile: Profile, updateCallback: ProfileUpdateCallback, onlyEditable: Boolean = false)

    fun getProfile(id: String, fetchCallback: ProfileFetchCallback)

    fun loadProfiles(fetchCallback: ProfileFetchCallback)

    fun saveProfiles(profiles: List<Profile>, updateCallback: ProfileUpdateCallback)

    fun addListener(callback: ProfileChangesCallback)
}