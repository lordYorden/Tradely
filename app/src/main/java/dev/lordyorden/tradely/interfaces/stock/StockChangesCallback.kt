package dev.lordyorden.tradely.interfaces.stock

import dev.lordyorden.tradely.models.Stock

interface StockChangesCallback {
    fun onStockChanged(stock: Stock)
    fun onStockRemoved(symbol: String)
    fun onStockAdded(stock: Stock)
}