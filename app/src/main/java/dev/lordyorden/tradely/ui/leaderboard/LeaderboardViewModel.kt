package dev.lordyorden.tradely.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.interfaces.profile.ProfileDataChange
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager

class LeaderboardViewModel : ViewModel() {

    private val profileManager = ProfileManager.getInstance()

    private val _profiles = MutableLiveData<List<Profile>?>(null)
    val profiles: LiveData<List<Profile>?>
        get() = _profiles

    init {
        profileManager.registerObserver(object : ProfileDataChange{
            override fun onDataChange() {
                setProfiles(profileManager.profiles)
            }
        })
    }

    fun setProfiles(profiles: List<Profile>) {
        _profiles.value = filterProfilesById(profileManager.myProfile.id, profiles).sortedBy { it.netWorth }.reversed()
    }

    private fun filterProfilesById(id: String, profiles: List<Profile>): List<Profile> {
        return profiles.filter { profile -> profile.id != id }
    }

    fun filterProfilesName(filter: String, profiles: List<Profile>): List<Profile> {
        return profiles.filter { profile -> profile.name.contains(filter) }
    }
}