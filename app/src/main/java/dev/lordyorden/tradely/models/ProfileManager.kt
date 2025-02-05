package dev.lordyorden.tradely.models

import android.content.Context
import android.util.Log
import dev.lordyorden.tradely.db.ProfileFirestoreDB
import dev.lordyorden.tradely.interfaces.profile.ProfileDB
import dev.lordyorden.tradely.interfaces.profile.ProfileFetchCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileUpdateCallback
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
    val db: ProfileDB = ProfileFirestoreDB()

    var myProfile: Profile = Profile.Builder()
        .id(UUID.fromString("a0d2bab4-16e3-4d23-93d3-ac144c0b086f"))
        .build()

//    var profiles = mutableListOf<Profile>()
//        private set

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
                .id(UUID.fromString("6453a9bd-95a2-4045-9d99-c76f64b53f1f"))
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

//    private fun onFinishLoading() {
//        myProfile = profiles.find { it.id == myProfile.id } ?: myProfile
//    }

    private fun updateProfile(profile: Profile){
        db.updateProfile(profile, object : ProfileUpdateCallback {
            override fun onUpdateSuccess(id: String) {
                Log.d("Profile DB Update", "Profile updated $id")
            }

            override fun onUpdateFailed(id: String) {
                Log.d("Profile DB Update", "Error updating profile $id")
            }
        })
    }

/*    private fun updateProfile(newProfile: Profile, fields: SetOptions = Constants.DB.PROFILE_UPLOAD_DEFAULT_OPTIONS){
        val profileDoc = db.collection(Constants.DB.PROFILES_REF)
        profileDoc.document(newProfile.id.toString())
            .set(newProfile, fields)
            .addOnSuccessListener {
                Log.d("My Profile Updated", "Profile: ${newProfile.id}")
            }
            .addOnFailureListener {
                Log.d("My Profile Update failed!", "Error updating ${newProfile.id}")
            }
    }*/

    /*private fun getProfile(id: String, fetchCallback: ProfileFetchCallback){
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
    }*/

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
                    profile.followers.remove(myProfile.id.toString())
                } else {
                    profile.followers.add(myProfile.id.toString())
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
        return profile.followers.find { it == myProfile.id.toString() } != null
    }

    fun loadProfiles(fetchCallback: ProfileFetchCallback) {
        db.loadProfiles(fetchCallback)
        db.getProfile(myProfile.id.toString(), object : ProfileFetchCallback {
            override fun onProfileFetch(profile: Profile) {
                myProfile = profile
            }

            override fun onProfileFetchFailed() {
                Log.d("Profile Fetch", "Error fetching my profile")
            }

        })
    }

/*    fun loadProfilesFromFirestore() {
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

    }*/

    fun saveProfiles() {
//        val profilesCollection = db.collection(Constants.DB.PROFILES_REF)
//        generateProfileList().forEach { profile ->
//            val documentId = profile.id.toString()
//            //moviesCollection.add(movie) //also works
//            profilesCollection.document(documentId)
//                .set(profile, Constants.DB.PROFILE_UPLOAD_DEFAULT_OPTIONS)
//                .addOnSuccessListener {
//                    Log.d("profiles Saved To DB", "Movie: $documentId")
//                }
//                .addOnFailureListener {
//                    Log.d("profiles Save failed!", "Error saving $documentId")
//                }
//        }
        db.saveProfiles(generateProfileList(), object : ProfileUpdateCallback {
            override fun onUpdateSuccess(id: String) {
                Log.d("Profile DB Update", "Profile updated $id")
            }

            override fun onUpdateFailed(id: String) {
                Log.d("Profile DB Update", "Error updating profile $id")
            }
        })

    }
}