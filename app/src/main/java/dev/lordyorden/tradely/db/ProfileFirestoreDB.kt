package dev.lordyorden.tradely.db

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dev.lordyorden.tradely.interfaces.profile.ProfileChangesCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileDB
import dev.lordyorden.tradely.interfaces.profile.ProfileFetchCallback
import dev.lordyorden.tradely.interfaces.profile.ProfileUpdateCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.utilities.Constants

class ProfileFirestoreDB : ProfileDB {

    private val db = Firebase.firestore
    var profileParams = Constants.DB.PROFILE_UPLOAD_DEFAULT_OPTIONS
        private set

    override fun updateProfile(newProfile: Profile, updateCallback: ProfileUpdateCallback) {
        updateProfile(newProfile, profileParams, updateCallback)
    }

    private fun updateProfile(newProfile: Profile, fields: SetOptions, updateCallback: ProfileUpdateCallback){
        val profileDoc = db.collection(Constants.DB.PROFILES_REF)
        profileDoc.document(newProfile.id)
            .set(newProfile, fields)
            .addOnSuccessListener {
                Log.d("My Profile Updated", "Profile: ${newProfile.id}")
                updateCallback.onUpdateSuccess(newProfile.id)
            }
            .addOnFailureListener {
                Log.d("My Profile Update failed!", "Error updating ${newProfile.id}")
                updateCallback.onUpdateFailed(newProfile.id)
            }
    }

    override fun getProfile(id: String, fetchCallback: ProfileFetchCallback) {
        val profileCollection = db.collection(Constants.DB.PROFILES_REF)
        profileCollection.document(id)
            .get()
            .addOnSuccessListener { result ->
                buildProfile(result.id, result)?.let {
                    fetchCallback.onProfileFetch(it)
                } ?: fetchCallback.onProfileFetchFailed()
                //Log.d("Profile fetched", "Profile: ${result.id}")

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
                    buildProfile(document.id, document)?.let {
                        fetchCallback.onProfileFetch(it)
                    }
                    //Log.d("Loaded", "${document.id} => ${document.data}")

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
            val documentId = profile.id
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

    override fun addListener(callback: ProfileChangesCallback) {
        db.collection(Constants.DB.PROFILES_REF)
            .addSnapshotListener { snapshot, e ->
                if (e != null)
                    Log.e("Firestore", "Error Listening. $e")
                val changes = StringBuilder()

                snapshot?.documentChanges?.forEach { dc ->
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            changes.append("ADDED: ${dc.document.data} \n")
                            buildProfile(dc.document.id, dc.document)?.let {
                                callback.onProfileAdded(
                                    it
                                )
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            changes.append("REMOVED: ${dc.document.data} \n")
                            callback.onProfileRemoved(dc.document.id)
                        }

                        DocumentChange.Type.MODIFIED -> {
                            changes.append("MODIFIED: ${dc.document.data} \n")
                            buildProfile(dc.document.id, dc.document)?.let {
                                callback.onProfileChanged(
                                    it
                                )
                            }
                        }
                    }
                    Log.d("listener", "listenToChangesOnFirestore: $changes")
                }
            }
    }


    private fun buildProfile(id: String, document: DocumentSnapshot): Profile? {
        val profile: Profile? = document.toObject<Profile>()
        profile?.id = id
        return profile
    }
}