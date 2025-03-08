package dev.lordyorden.tradely.interfaces.profile

import dev.lordyorden.tradely.models.Profile

interface ProfileDataChange {
    fun onDataChange(profile: Profile)
}