package dev.lordyorden.tradely.ui.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.interfaces.stock.StockChangesCallback
import dev.lordyorden.tradely.interfaces.stock.StockFetchCallback
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockManager
import java.util.Locale

class ExploreStocksViewModel : ViewModel() {

    private val stockManager = StockManager.getInstance()

    var rawStocks: List<Stock>? = null

    private val _stocks = MutableLiveData<List<Stock>?>(null)
    val stocks: LiveData<List<Stock>?>
        get() = _stocks

    init {
        stockManager.db.registerStocks(object: StockChangesCallback{
            override fun onStockChanged(stock: Stock) {
                if (_stocks.value != null)
                    setStocks(filterStocksBySymbol(stock.symbol, _stocks.value!!) + stock)
            }

            override fun onStockRemoved(symbol: String) {
                if (_stocks.value != null)
                    setStocks(filterStocksBySymbol(symbol, _stocks.value!!))
            }

            override fun onStockAdded(stock: Stock) {
                if (_stocks.value != null)
                    setStocks(_stocks.value!! + stock)
                else{
                    setStocks(listOf(stock))
                }
            }

        })
    }

    private fun filterStocksBySymbol(symbol: String, stocks: List<Stock>): List<Stock> {
        return stocks.filter { stock -> stock.symbol != symbol }
    }

    fun setStocks(stocks: List<Stock>) {
        _stocks.value = stocks.sortedBy { it.change }.reversed()
    }

    fun searchStocks(symbol: String){
            if(rawStocks == null)
                rawStocks = _stocks.value!!
            else
                setStocks(rawStocks!!.filter { stock -> stock.symbol.startsWith(symbol.uppercase(Locale.getDefault())) })
    }

    fun deepSearch(keyword: String){
        stockManager.searchStock(keyword, object: StockFetchCallback{
            override fun onStockFetch(stock: Stock) {
                addStock(stock)
            }

            override fun onStockFetchFailed() {
                Log.e("Search result", "noting was found")
            }

        })
    }

    fun addStock(stock: Stock) {
        if (_stocks.value != null)
            _stocks.postValue(_stocks.value!! + stock)
        else{
            _stocks.postValue(listOf(stock))
        }
    }

    fun stopSearch() {
        rawStocks?.let { setStocks(it) }
        rawStocks = null
    }


//    private val profileManager = ProfileManager.getInstance()
//    private val _profiles = MutableLiveData<List<Profile>?>(null)

    /*val profiles: LiveData<List<Profile>?>
        get() = _profiles

    init {
        profileManager.db.addListener(object : ProfileChangesCallback {
            override fun onProfileChanged(profile: Profile) {
                if (_profiles.value != null)
                    setProfiles(filterProfilesById(profile.id, _profiles.value!!) + profile)
            }

            override fun onProfileRemoved(id: String) {
                if (_profiles.value != null)
                    setProfiles(filterProfilesById(id, _profiles.value!!))
            }

            override fun onProfileAdded(profile: Profile) {
                if (_profiles.value != null)
                    setProfiles(_profiles.value!! + profile)
            }

        })

        profileManager.loadProfiles(object : ProfileFetchCallback {

            val profilesLoaded: MutableList<Profile> = mutableListOf()

            override fun onProfileFetch(profile: Profile) {
                profilesLoaded.add(profile)
            }

            override fun onProfileFetchFailed() {
                Log.d("Profile fetch", "Error fetching profiles")
            }

            override fun onFetchComplete() {
                setProfiles(profilesLoaded)
            }
        })
    }

    fun setProfiles(profiles: List<Profile>) {
        _profiles.value = filterProfilesById(profileManager.myProfile.id, profiles).sortedBy { it.netWorth }.reversed()
    }

    fun filterProfilesById(id: String, profiles: List<Profile>): List<Profile> {
        return profiles.filter { profile -> profile.id != id }
    }*/
}