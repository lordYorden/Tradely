package dev.lordyorden.tradely.ui.home.profile_card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.interfaces.profile.ProfileDataChange
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager

class ProfileViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<Profile>()
    val selectedProfile: LiveData<Profile> get() = mutableSelectedItem

    fun selectProfile(profile: Profile) {
        mutableSelectedItem.value = profile
        setListener()
    }

    private fun setListener() {
        ProfileManager.getInstance().registerObserver(object : ProfileDataChange {
            override fun onDataChange() {
                mutableSelectedItem.value =
                    ProfileManager.getInstance().profiles.firstOrNull { profile -> profile.id == mutableSelectedItem.value?.id }
            }
        })
    }
}