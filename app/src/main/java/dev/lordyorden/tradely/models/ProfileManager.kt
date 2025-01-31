package dev.lordyorden.tradely.models

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dev.lordyorden.tradely.interfaces.ProfileFetchCallback
import dev.lordyorden.tradely.utilities.Constants
import java.lang.ref.WeakReference
import java.util.UUID

class ProfileManager private constructor(context: Context) {

    companion object {

        @Volatile
        private var instance: ProfileManager? = null

        fun getInstance(): ProfileManager {
            return instance
                ?: throw IllegalStateException("ProfileManager must be initialized by calling init(context) before use")
        }

        fun init(context: Context): ProfileManager {
            return instance ?: synchronized(this) {
                instance ?: ProfileManager(context).also { instance = it }
            }
        }
    }

    private val contextRef = WeakReference(context)
    private val db = Firebase.firestore

    var myProfile: Profile = Profile.Builder()
        .id(UUID.fromString("6453a9bd-95a2-4045-9d99-c76f64b53f1f"))
        .build()

    var profiles = mutableListOf<Profile>()
        private set

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
                .id(UUID.fromString("6453a9bd-95a2-4045-9d99-c76f64b53f1f"))
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
                .change(1.5)
                .build()
        )

        return profiles

    }

    private fun onFinishLoading() {
        myProfile = profiles.find { it.id == myProfile.id } ?: myProfile
    }

    private fun updateProfile(newProfile: Profile, fields: SetOptions = Constants.DB.PROFILE_UPLOAD_DEFAULT_OPTIONS){
        val profileDoc = db.collection(Constants.DB.PROFILES_REF)
        profileDoc.document(newProfile.id.toString())
            .set(newProfile, fields)
            .addOnSuccessListener {
                Log.d("My Profile Updated", "Profile: ${newProfile.id}")
            }
            .addOnFailureListener {
                Log.d("My Profile Update failed!", "Error updating ${newProfile.id}")
            }
    }

    private fun getProfile(id: String, fetchCallback: ProfileFetchCallback){
        val profileCollection = db.collection(Constants.DB.PROFILES_REF)
        profileCollection.document(id)
            .get()
            .addOnSuccessListener { result ->
                val profile: Profile? = result.toObject<Profile>()
                if (profile != null){
                    profile.id = UUID.fromString(result.id)
                    Log.d("Profile fetched", "Profile: ${profile.id}")
                    fetchCallback.onProfileFetch(profile)
                }
            }
            .addOnFailureListener {
                Log.d("Profile fetch failed!", "Error fetching $id")
                fetchCallback.onProfileFetchFailed()
            }
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
        getProfile(id, object : ProfileFetchCallback{
            override fun onProfileFetch(profile: Profile) {
                if (isFollowerOf(profile)) {
                    profile.followers.remove(myProfile.id.toString())
                } else {
                    profile.followers.add(myProfile.id.toString())
                }
                updateProfile(profile)
            }

            override fun onProfileFetchFailed() {
                Log.d("Error", "Error setting follower for $id")
            }

        })
    }

    fun isFollowing(id: String): Boolean {
        return myProfile.following.find { it == id } != null
    }

    fun isFollowerOf(profile: Profile): Boolean {
        return profile.followers.find { it == myProfile.id.toString() } != null
    }

    fun loadProfilesFromFirestore() {
        val list: MutableList<Profile> = mutableListOf()

        val profileCollection = db.collection(Constants.DB.PROFILES_REF)
        profileCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val profile = document.toObject<Profile>()
                    profile.id = UUID.fromString(document.id)
                    list.add(profile)
                    Log.d("Loaded", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "Error getting documents.", exception)
            }.addOnCompleteListener {
                profiles = list
                onFinishLoading()
            }

    }

    fun saveToFirestore() {
        val profilesCollection = db.collection(Constants.DB.PROFILES_REF)
        generateProfileList().forEach { profile ->
            val documentId = profile.id.toString()
            //moviesCollection.add(movie) //also works
            profilesCollection.document(documentId)
                .set(profile, Constants.DB.PROFILE_UPLOAD_DEFAULT_OPTIONS)
                .addOnSuccessListener {
                    Log.d("profiles Saved To DB", "Movie: $documentId")
                }
                .addOnFailureListener {
                    Log.d("profiles Save failed!", "Error saving $documentId")
                }
        }

    }
}