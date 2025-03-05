package dev.lordyorden.tradely.ui.stock_info

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.CandleEntry
import dev.lordyorden.tradely.models.StockParser
import dev.lordyorden.tradely.models.TestDataProvider

class StockInfoViewModel : ViewModel() {

    val stockParser = StockParser()

    fun getEntriesMonthly(): List<CandleEntry>{
        return stockParser.parseMonthlyData(TestDataProvider.getMonthlyData())?.toMutableList() ?: mutableListOf()
    }

    fun getEntriesWeekly(): List<CandleEntry>{
        return stockParser.parseWeeklyData(TestDataProvider.getWeeklyData())?.toMutableList() ?: mutableListOf()
    }

    fun getEntriesDaily(): List<CandleEntry>{
        return stockParser.parseDailyData(TestDataProvider.getDailyData())?.toMutableList() ?: mutableListOf()
    }
}