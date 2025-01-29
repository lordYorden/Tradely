package dev.lordyorden.tradely

import android.app.Application
import dev.lordyorden.tradely.models.ProfileManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ProfileManager.init(this)
        //ProfileManager.getInstance().saveToFirestore()
        ProfileManager.getInstance().loadDataFromFirestore()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}