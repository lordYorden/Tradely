package dev.lordyorden.tradely.ui.stock_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.models.Stock

class StockViewModel: ViewModel() {
    private val mutableSelectedItem = MutableLiveData<Stock>()
    val selectedStock: LiveData<Stock> get() = mutableSelectedItem

    fun selectStock(stock: Stock) {
        mutableSelectedItem.value = stock
    }
}