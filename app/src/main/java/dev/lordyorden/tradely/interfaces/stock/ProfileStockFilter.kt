package dev.lordyorden.tradely.interfaces.stock

import dev.lordyorden.tradely.models.Profile

interface ProfileStockFilter {
    fun filterStocks(profile: Profile): Set<String>
}