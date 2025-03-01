package dev.lordyorden.tradely.interfaces.stock

import dev.lordyorden.tradely.models.Stock

interface StockFetchCallback {
    fun onStockFetch(stock: Stock)
    fun onStockFetchFailed()
}