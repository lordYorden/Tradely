package dev.lordyorden.tradely.interfaces.profile

import dev.lordyorden.tradely.models.Profile

interface ProfileCallback {
    fun onFollowClicked(profile: Profile, position: Int)
}