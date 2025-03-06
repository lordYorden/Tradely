package dev.lordyorden.tradely.interfaces.stock

import com.github.mikephil.charting.data.CandleEntry

interface StockPriceUpdateCallback {
    fun onPricesUpdate(prices: List<CandleEntry>, type: String)
}