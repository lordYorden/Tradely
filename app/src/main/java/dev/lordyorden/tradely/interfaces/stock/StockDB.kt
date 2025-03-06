package dev.lordyorden.tradely.interfaces.stock

import com.github.mikephil.charting.data.CandleEntry
import dev.lordyorden.tradely.models.Stock

interface StockDB {

    fun updateStock(stock: Stock)

    fun getStock(symbol: String, fetchCallback: StockFetchCallback)

    fun registerStocks(callback: StockChangesCallback)

    fun saveStocks(stocks: List<Stock>)

    fun updateStockPrices(symbol: String, prices: List<CandleEntry>, type: String)

    fun registerUpdateOnPrice(symbol: String, callback: StockPriceUpdateCallback)
}