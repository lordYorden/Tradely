package dev.lordyorden.tradely.ui.watchlist.filter

import androidx.lifecycle.LiveData
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.Stock

interface FilterStocksViewModel {
    fun getStocksLive(): LiveData<List<Stock>>
    fun onProfileChanged(profile: Profile)
    fun onStockChange()
}