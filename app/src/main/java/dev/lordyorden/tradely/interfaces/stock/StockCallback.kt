package dev.lordyorden.tradely.interfaces.stock

import dev.lordyorden.tradely.models.Stock

interface StockCallback {
    fun onEditClicked(stock: Stock, position: Int)
}