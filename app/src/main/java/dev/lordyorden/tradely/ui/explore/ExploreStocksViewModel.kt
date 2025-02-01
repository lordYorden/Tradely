package dev.lordyorden.tradely.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockManager

class ExploreStocksViewModel : ViewModel() {

    private val _stocks = MutableLiveData<List<Stock>>().apply {
        value = StockManager.getInstance().stocks
    }
    val stocks: LiveData<List<Stock>> = _stocks
}