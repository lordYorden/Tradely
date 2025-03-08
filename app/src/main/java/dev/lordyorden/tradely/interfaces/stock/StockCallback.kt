package dev.lordyorden.tradely.interfaces.stock

import dev.lordyorden.tradely.models.Stock

interface StockCallback {
    fun onExtraClicked(stock: Stock, position: Int)
    fun onStockClicked(stock: Stock, position: Int)
}