package dev.lordyorden.tradely.ui.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.interfaces.stock.ProfileStockFilter
import dev.lordyorden.tradely.interfaces.stock.StockUpdateCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockManager
import dev.lordyorden.tradely.ui.watchlist.filter.FilterStocksViewModel

class WatchlistViewModel: ViewModel(), FilterStocksViewModel {
    private val stockManager = StockManager.getInstance()
    //private val profileManager = ProfileManager.getInstance()

    private val _stocks = MutableLiveData<List<Stock>>(listOf())
    val stocks: LiveData<List<Stock>>
        get() = _stocks

    init {
        stockManager.registerObserver(object: StockUpdateCallback {
            override fun updateStocks() {
                useFilter()
            }
        })

        //todo add myProfile observer
//        profileManager.registerObserver(object : ProfileDataChange {
//            override fun onDataChange(profile: Profile) {
//                useFilter(profile)
//            }
//        })
    }

    private fun getFilter(): ProfileStockFilter {
        return object : ProfileStockFilter {
            override fun filterStocks(profile: Profile): Set<String> {
                return profile.watchlist.toSet()
            }
        }
    }

    fun useFilter(profile: Profile = ProfileManager.getInstance().myProfile) {
        setStocks(stockManager.stocks, getFilter().filterStocks(profile)) //?: Log.e("FilterStocksViewModel", "Filter not set")
    }

    private fun setStocks(stocks: List<Stock>, filter: Set<String> = setOf()) {
        _stocks.postValue(stocks.filter { stock -> filter.contains(stock.symbol) }.sortedBy { it.symbol }.reversed())
    }

    override fun getStocksLive(): LiveData<List<Stock>> {
        return stocks
    }
}
