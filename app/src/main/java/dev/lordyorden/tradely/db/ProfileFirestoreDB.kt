package dev.lordyorden.tradely.db

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dev.lordyorden.tradely.interfaces.profile.ProfileDB
import dev.lordyorden.tradely.interfaces.profile.ProfileFetchCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileUpdateCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.utilities.Constants
import java.util.UUID

class ProfileFirestoreDB : ProfileDB {

    private val db = Firebase.firestore
    var profileParams = Constants.DB.PROFILE_UPLOAD_DEFAULT_OPTIONS
        private set

    override fun updateProfile(newProfile: Profile, updateCallback: ProfileUpdateCallback) {
        updateProfile(newProfile, profileParams, updateCallback)
    }

    private fun updateProfile(newProfile: Profile, fields: SetOptions, updateCallback: ProfileUpdateCallback){
        val profileDoc = db.collection(Constants.DB.PROFILES_REF)
        profileDoc.document(newProfile.id.toString())
            .set(newProfile, fields)
            .addOnSuccessListener {
                Log.d("My Profile Updated", "Profile: ${newProfile.id}")
                updateCallback.onUpdateSuccess(newProfile.id.toString())
            }
            .addOnFailureListener {
                Log.d("My Profile Update failed!", "Error updating ${newProfile.id}")
                updateCallback.onUpdateFailed(newProfile.id.toString())
            }
    }

    override fun getProfile(id: String, fetchCallback: ProfileFetchCallback) {
        val profileCollection = db.collection(Constants.DB.PROFILES_REF)
        profileCollection.document(id)
            .get()
            .addOnSuccessListener { result ->
                val profile = buildProfile(result.id, result)
                //Log.d("Profile fetched", "Profile: ${profile.id}")
                fetchCallback.onProfileFetch(profile)
//                //val profile: Profile? = result.toObject<Profile>()
//                if (profile != null){
//                    profile.id = UUID.fromString(result.id)
//                    Log.d("Profile fetched", "Profile: ${profile.id}")
//                    fetchCallback.onProfileFetch(profile)
//                }
            }
            .addOnFailureListener {
                //Log.d("Profile fetch failed!", "Error fetching $id")
                fetchCallback.onProfileFetchFailed()
            }
    }

    override fun loadProfiles(fetchCallback: ProfileFetchCallback) {
        //val list: MutableList<Profile> = mutableListOf()
        val profileCollection = db.collection(Constants.DB.PROFILES_REF)
        profileCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    val profile = document.toObject<Profile>()
//                    profile.id = UUID.fromString(document.id)
                    val profile = buildProfile(document.id, document)
                    //Log.d("Loaded", "${document.id} => ${document.data}")
                    fetchCallback.onProfileFetch(profile)
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("Error", "Error getting documents.", exception)
                fetchCallback.onProfileFetchFailed()
            }.addOnCompleteListener {
                //profiles = list
                fetchCallback.onFetchComplete()
            }
    }

    override fun saveProfiles(profiles: List<Profile>, updateCallback: ProfileUpdateCallback) {
        val profilesCollection = db.collection(Constants.DB.PROFILES_REF)
        profiles.forEach { profile ->
            val documentId = profile.id.toString()
            //moviesCollection.add(movie) //also works
            profilesCollection.document(documentId)
                .set(profile, profileParams)
                .addOnSuccessListener {
                    //Log.d("profiles Saved To DB", "Profile: $documentId")
                    updateCallback.onUpdateSuccess(documentId)
                }
                .addOnFailureListener {
                    //Log.d("profiles Save failed!", "Error saving $documentId")
                    updateCallback.onUpdateFailed(documentId)
                }
        }
    }

    private fun buildProfile(id: String, document: DocumentSnapshot): Profile {
        val profile: Profile = document.toObject<Profile>()!!
        profile.id = UUID.fromString(id)
        return profile
    }
}