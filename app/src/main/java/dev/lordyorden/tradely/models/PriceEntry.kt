package dev.lordyorden.tradely.models

import com.github.mikephil.charting.data.CandleEntry

class PriceEntry(dateFloat: Float, high: Float, low: Float, open: Float , close: Float): CandleEntry(dateFloat, high, low, open, close) {
    constructor() : this(0f, 0f, 0f, 0f, 0f)
}