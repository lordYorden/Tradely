package dev.lordyorden.tradely.ui.home.profile_card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.models.Profile

class ProfileViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<Profile>()
    val selectedProfile: LiveData<Profile> get() = mutableSelectedItem

    fun selectProfile(profile: Profile) {
        mutableSelectedItem.value = profile
    }
}