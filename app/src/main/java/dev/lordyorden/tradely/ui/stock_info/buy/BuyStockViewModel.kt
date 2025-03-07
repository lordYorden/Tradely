package dev.lordyorden.tradely.ui.stock_info.buy

import androidx.lifecycle.ViewModel
import dev.lordyorden.tradely.models.Stock

class BuyStockViewModel : ViewModel() {

    var currentStock: Stock? = null
        private set

    fun setStockInfo(stock: Stock?) {
        currentStock = stock
    }
}