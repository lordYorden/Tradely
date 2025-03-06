package dev.lordyorden.tradely.ui.stock_info

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.CandleEntry
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockParser
import dev.lordyorden.tradely.models.TestDataProvider

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

    fun getEntriesHourly() : List<CandleEntry>{
        return StockParser.parseHourlyData(TestDataProvider.getHourlyData())?.toMutableList() ?: mutableListOf()
    }

//    fun getEntriesMonthly(): List<CandleEntry>{
//        return StockParser.parseMonthlyData(TestDataProvider.getMonthlyData())?.toMutableList() ?: mutableListOf()
//    }
//
//    fun getEntriesWeekly(): List<CandleEntry>{
//        return StockParser.parseWeeklyData(TestDataProvider.getWeeklyData())?.toMutableList() ?: mutableListOf()
//    }
//
//    fun getEntriesDaily(): List<CandleEntry>{
//        return StockParser.parseDailyData(TestDataProvider.getDailyData())?.toMutableList() ?: mutableListOf()
//    }
}