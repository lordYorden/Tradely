package dev.lordyorden.tradely.models

import android.content.Context
import android.util.Log
import dev.lordyorden.tradely.interfaces.profile.ProfileChangesCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileDB
import dev.lordyorden.tradely.interfaces.profile.ProfileDataChange
import dev.lordyorden.tradely.interfaces.profile.ProfileFetchCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileUpdateCallback
import java.lang.ref.WeakReference

class ProfileManager private constructor(context: Context, val db: ProfileDB) {

    companion object {

        @Volatile
        private var instance: ProfileManager? = null

        fun getInstance(): ProfileManager {
            return instance
                ?: throw IllegalStateException("ProfileManager must be initialized by calling init(context) before use")
        }

        fun init(context: Context, profileDB: ProfileDB): ProfileManager {
            return instance ?: synchronized(this) {
                instance ?: ProfileManager(context, profileDB).also { instance = it }
            }
        }

    }

    private val contextRef = WeakReference(context)


    val profiles: MutableList<Profile> = mutableListOf()

    fun filterProfilesById(id: String) {
        profiles.removeIf { profile -> profile.id == id }
    }

    fun addListener(){
        db.addListener(object : ProfileChangesCallback{
            override fun onProfileChanged(profile: Profile) {
                if (profiles.isNotEmpty()){
                    filterProfilesById(profile.id)
                }
                profiles.add(profile)
                notifyObservers()
            }

            override fun onProfileRemoved(id: String) {
                if (profiles.isNotEmpty()){
                    filterProfilesById(id)
                }
                notifyObservers()
            }

            override fun onProfileAdded(profile: Profile) {
                profiles.add(profile)
                notifyObservers()
            }

        })
    }

    private fun updateProfile(profile: Profile) {
        db.updateProfile(profile, object : ProfileUpdateCallback {
            override fun onUpdateSuccess(id: String) {
                Log.d("Profile DB Update", "Profile updated $id")
            }

            override fun onUpdateFailed(id: String) {
                Log.d("Profile DB Update", "Error updating profile $id")
            }
        })
    }

    private val observers: MutableList<ProfileDataChange> = mutableListOf()
    fun registerObserver(callback: ProfileDataChange){
        observers.add(callback)
        callback.onDataChange()
    }

    private fun notifyObservers() {
        observers.forEach{
            it.onDataChange()
        }
    }


    var myProfile: Profile = Profile.Builder()
        .id("a0d2bab4-16e3-4d23-93d3-ac144c0b086f")
        .build()

    fun loadOrCreateProfile(profile: Profile) {
        val id = profile.id
        db.getProfile(id, object : ProfileFetchCallback {
            override fun onProfileFetch(profile: Profile) {
                myProfile = profile
                notifyObservers()
                Log.d("Profile loaded", "profile was loaded from db for $id")
            }

            override fun onProfileFetchFailed() {
                db.updateProfile(profile, object : ProfileUpdateCallback {
                    override fun onUpdateSuccess(id: String) {
                        Log.d("New Profile", "Profile created for $id")
                        myProfile = profile
                        notifyObservers()
                    }

                    override fun onUpdateFailed(id: String) {
                        Log.e("New Profile", "Profile creation failed for $id")
                    }

                })
            }

        })
    }

    fun buyStock(symbol: String, amount: Double){
        val currAmount = myProfile.bought.getOrDefault(symbol, 0.0)
        myProfile.bought[symbol] = currAmount + amount
        updateProfile(myProfile)
        notifyObservers()
    }

    fun addToWatchlist(symbol: String){
        myProfile.watchlist.add(symbol)
        updateProfile(myProfile)
        notifyObservers()
    }

    fun toggleFollow(id: String) {
        if (isFollowing(id)) {
            myProfile.following.remove(id)
        } else {
            myProfile.following.add(id)
        }
        updateProfile(myProfile)
        toggleFollowerOf(id)
    }

    private fun toggleFollowerOf(id: String) {
        db.getProfile(id, object : ProfileFetchCallback {
            override fun onProfileFetch(profile: Profile) {
                if (isFollowerOf(profile)) {
                    profile.followers.remove(myProfile.id)
                } else {
                    profile.followers.add(myProfile.id)
                }
                updateProfile(profile)
            }

            override fun onProfileFetchFailed() {
                Log.d("Profile Fetch", "Error setting follower for $id")
            }

        })
    }

    fun isFollowing(id: String): Boolean {
        return myProfile.following.find { it == id } != null
    }

    fun isFollowerOf(profile: Profile): Boolean {
        return profile.followers.find { it == myProfile.id } != null
    }

//    fun loadProfiles(fetchCallback: ProfileFetchCallback) {
//        db.loadProfiles(fetchCallback)
//        db.getProfile(myProfile.id, object : ProfileFetchCallback {
//            override fun onProfileFetch(profile: Profile) {
//                myProfile = profile
//                notifyObservers()
//            }
//
//            override fun onProfileFetchFailed() {
//                Log.d("Profile Fetch", "Error fetching my profile")
//            }
//
//        })
//    }

    fun saveProfiles() {
        db.saveProfiles(generateProfileList(), object : ProfileUpdateCallback {
            override fun onUpdateSuccess(id: String) {
                Log.d("Profile DB Update", "Profile updated $id")
            }

            override fun onUpdateFailed(id: String) {
                Log.d("Profile DB Update", "Error updating profile $id")
            }
        })

    }

    private fun generateProfileList(): List<Profile> {
        val profiles = mutableListOf<Profile>()
        profiles.add(
            Profile
                .Builder()
                .profilePic("")
                .name("Elon Musk")
                .netWorth(151_000_000_000.0)
                .country("United States")
                .description("Tesla, SpaceX")
                .change(2.5)
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/c/cb/Elon_Musk_Royal_Society_crop.jpg")
                .build()
        )

        profiles.add(
            Profile
                .Builder()
                .profilePic("")
                .name("Elon Tusk")
                .netWorth(151_000_000_000.0)
                .country("United States")
                .description("Tesla, SpaceX")
                .change(2.5)
                .id("6453a9bd-95a2-4045-9d99-c76f64b53f1f")
                .profilePic("https://static.wikia.nocookie.net/rickandmorty/images/e/ee/S4e3_2019-11-28-13h17m16s986.png/revision/latest?cb=20191128184507")
                .build()
        )

        profiles.add(
            Profile
                .Builder()
                .profilePic("")
                .name("Jeff Bezos")
                .netWorth(177_000_000_000.0)
                .country("United States")
                .description("Amazon")
                .change(1.5)
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/6/6c/Jeff_Bezos_at_Amazon_Spheres_Grand_Opening_in_Seattle_-_2018_%2839074799225%29_%28cropped%29.jpg")
                .build()
        )
        profiles.add(
            Profile
                .Builder()
                .profilePic("")
                .name("Bernard Arnault")
                .netWorth(150_000_000_000.0)
                .country("France")
                .description("LVMH")
                .change(1.5)
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/a/a5/Bernard_Arnault_%282%29_-_2017_%28cropped%29.jpg")
                .build()
        )
        profiles.add(
            Profile
                .Builder()
                .profilePic("")
                .name("Bill Gates")
                .netWorth(124_000_000_000.0)
                .country("United States")
                .description("Microsoft")
                .change(-1.5)
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/a/a0/Bill_Gates_2018.jpg")
                .build()
        )
        profiles.add(
            Profile
                .Builder()
                .profilePic("")
                .name("Mark Zuckerberg")
                .netWorth(97_000_000_000.0)
                .country("United States")
                .description("Facebook")
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/1/18/Mark_Zuckerberg_F8_2019_Keynote_%2832830578717%29_%28cropped%29.jpg")
                .change(1.5)
                .build()
        )

        return profiles

    }
}