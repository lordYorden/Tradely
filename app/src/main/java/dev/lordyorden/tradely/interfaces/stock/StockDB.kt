package dev.lordyorden.tradely.interfaces.stock

import dev.lordyorden.tradely.models.Stock

interface StockDB {

    fun updateStock(stock: Stock)

    fun getStock(symbol: String, fetchCallback: StockFetchCallback)

    fun registerStocks(callback: StockChangesCallback)

    fun saveStocks(stocks: List<Stock>)
}