package dev.lordyorden.tradely.ui.stock_info

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.CandleEntry
import dev.lordyorden.tradely.models.Stock

class StockInfoViewModel : ViewModel() {

    val monthly: MutableList<CandleEntry> = mutableListOf()
    val weekly: MutableList<CandleEntry> = mutableListOf()
    val daily: MutableList<CandleEntry> = mutableListOf()
    val hourly: MutableList<CandleEntry> = mutableListOf()

    var currentStock: Stock? = null
        private set

    fun setStockInfo(stock: Stock){
        currentStock = stock

        monthly.clear()
        monthly.addAll(stock.monthly)

        weekly.clear()
        weekly.addAll(stock.weekly)

        daily.clear()
        daily.addAll(stock.daily)

        hourly.clear()
        hourly.addAll(stock.hourly)
    }
}