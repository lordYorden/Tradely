package dev.lordyorden.tradely.interfaces

import dev.lordyorden.tradely.models.Profile

interface ProfileCallback {
    fun onFollowClicked(profile: Profile, position: Int)
}