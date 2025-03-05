package dev.lordyorden.tradely.ui.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.db.StockRealtimeDB
import dev.lordyorden.tradely.interfaces.stock.StockFetchCallback
import dev.lordyorden.tradely.interfaces.stock.StockUpdateCallback
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockManager
import java.util.Locale

class ExploreStocksViewModel : ViewModel() {

    private val stockManager = StockManager(StockRealtimeDB())

    private val _stocks = MutableLiveData<List<Stock>>(listOf())
    val stocks: LiveData<List<Stock>>
        get() = _stocks

    init {
        stockManager.registerStocks(object: StockUpdateCallback{
            override fun updateStocks() {
                setStocks(stockManager.stocks)
            }
        })
    }

    fun setStocks(stocks: List<Stock>) {
        _stocks.postValue(stocks.sortedBy { it.symbol }.reversed())
    }

    fun searchStocks(symbol: String){
        val stocks = stockManager.stocks.filter { stock -> stock.symbol.startsWith(symbol, ignoreCase = true) || stock.description.contains(symbol, ignoreCase = true)}
        setStocks(stocks)
    }

    fun deepSearch(keyword: String){
        if (!_stocks.value.isNullOrEmpty()){
            return
        }

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
        setStocks(stockManager.stocks)
    }
}