package dev.lordyorden.tradely

import android.app.Application
import dev.lordyorden.tradely.db.ProfileFirestoreDB
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.utilities.HttpRequestHandler
import dev.lordyorden.tradely.utilities.ImageLoader

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ProfileManager.init(this, ProfileFirestoreDB())
        HttpRequestHandler.init(this)
        //ProfileManager.getInstance().saveProfiles()
        ImageLoader.init(this)
        //ProfileManager.getInstance().saveToFirestore()
        //ProfileManager.getInstance().loadProfilesFromFirestore()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}