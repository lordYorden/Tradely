package dev.lordyorden.tradely.ui.leaderboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.interfaces.profile.ProfileChangesCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileFetchCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager

class LeaderboardViewModel : ViewModel() {

    private val profileManager = ProfileManager.getInstance()
    private val _profiles = MutableLiveData<List<Profile>?>(null)

    val profiles: LiveData<List<Profile>?>
        get() = _profiles

    init {
        profileManager.db.addListener(object : ProfileChangesCallback{
            override fun onProfileChanged(profile: Profile) {
                if (_profiles.value != null)
                    setProfiles(filterProfilesById(profile.id, _profiles.value!!) + profile)
            }

            override fun onProfileRemoved(id: String) {
                if (_profiles.value != null)
                    setProfiles(filterProfilesById(id, _profiles.value!!))
            }

            override fun onProfileAdded(profile: Profile) {
                if (_profiles.value != null)
                    setProfiles(_profiles.value!! + profile)
            }

        })

        profileManager.loadProfiles(object : ProfileFetchCallback {

            val profilesLoaded: MutableList<Profile> = mutableListOf()

            override fun onProfileFetch(profile: Profile) {
                profilesLoaded.add(profile)
            }

            override fun onProfileFetchFailed() {
                Log.d("Profile fetch", "Error fetching profiles")
            }

            override fun onFetchComplete() {
                setProfiles(profilesLoaded)
            }
        })
    }

    fun setProfiles(profiles: List<Profile>) {
        _profiles.value = filterProfilesById(profileManager.myProfile.id, profiles).sortedBy { it.netWorth }.reversed()
    }

    fun filterProfilesById(id: String, profiles: List<Profile>): List<Profile> {
        return profiles.filter { profile -> profile.id != id }
    }

    fun filterProfilesName(filter: String, profiles: List<Profile>): List<Profile> {
        return profiles.filter { profile -> profile.name.contains(filter) }
    }

    /*// making this nullable so we can have a "no message" state
    private val _toastMessage = MutableLiveData<Long?>(null)
    // you should specify the type here btw, as LiveData instead of MutableLiveData -
// that's the reason for making the Mutable reference private and having a public version
    val toastMessage: LiveData<Long?>
        get() = _toastMessage

    // call this when the current message has been shown
    fun messageDisplayed() {
        _toastMessage.value = null
    }*/
}