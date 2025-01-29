package dev.lordyorden.tradely.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import java.util.UUID

class LeaderboardViewModel : ViewModel() {

    private var filteredProfiles: List<Profile> = ProfileManager.getInstance().profiles
    var isFiltered: Boolean = false;

    val _profiles = MutableLiveData<List<Profile>>().apply {
        value = filteredProfiles
    }
    val profiles: LiveData<List<Profile>> = _profiles

    fun filterProfilesById(id: UUID) {
        _profiles.apply {
            value = filteredProfiles.filter { profile -> profile.id != id }
        }
        isFiltered = true
    }

    fun filterProfilesName(filter: String) {
        filteredProfiles =  filteredProfiles.filter { profile -> profile.name.contains(filter) }
    }
}