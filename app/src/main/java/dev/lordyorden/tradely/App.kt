package dev.lordyorden.tradely

import android.app.Application
import dev.lordyorden.tradely.db.ProfileFirestoreDB
import dev.lordyorden.tradely.db.StockRealtimeDB
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.models.StockManager
import dev.lordyorden.tradely.utilities.ImageLoader

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ProfileManager.init(this, ProfileFirestoreDB())
        StockManager.init(this, StockRealtimeDB())
        //StockManager.getInstance().uploadStocks()
        //ProfileManager.getInstance().saveProfiles()
        ImageLoader.init(this)
        //ProfileManager.getInstance().saveToFirestore()
        //ProfileManager.getInstance().loadProfilesFromFirestore()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}