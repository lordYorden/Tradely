package dev.lordyorden.tradely.ui.watchlist.filter

import androidx.lifecycle.LiveData
import dev.lordyorden.tradely.models.Stock

interface FilterStocksViewModel {
    fun getStocksLive(): LiveData<List<Stock>>

//    protected val stockManager = StockManager.getInstance()
//    protected val profileManager = ProfileManager.getInstance()
//
//    abstract fun getFilter(): ProfileStockFilter?
//
//    private val _stocks = MutableLiveData<List<Stock>>(listOf())
//    val stocks: LiveData<List<Stock>>
//        get() = _stocks
//
//    init {
//        stockManager.registerObserver(object: StockUpdateCallback {
//            override fun updateStocks() {
//                useFilter()
//            }
//        })
//
//        profileManager.registerObserver(object : ProfileDataChange {
//            override fun onDataChange(profile: Profile) {
//                useFilter(profile)
//            }
//        })
//    }
//
//    fun useFilter(profile: Profile = ProfileManager.getInstance().myProfile) {
//        getFilter()?.let {
//            setStocks(stockManager.stocks, it.filterStocks(profile))
//        } ?: Log.e("FilterStocksViewModel", "Filter not set")
//    }
//
//    private fun setStocks(stocks: List<Stock>, filter: Set<String> = setOf()) {
//        _stocks.postValue(stocks.filter { stock -> filter.contains(stock.symbol) }.sortedBy { it.symbol }.reversed())
//    }
}