package dev.lordyorden.tradely.interfaces.profile

import dev.lordyorden.tradely.models.Profile

interface ProfileChangesCallback {
    fun onProfileChanged(profile: Profile)
    fun onProfileRemoved(id: String)
    fun onProfileAdded(profile: Profile)
}