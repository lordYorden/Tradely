package dev.lordyorden.tradely.models

data class Stock private constructor(
    val profilePic: String,
    var symbol: String,
    var pricePerShare: Double,
    var volume: Int,
    var change: Double,
    var marketCap: Int,
    var region: String,
    var currency: String,
    var description: String
){
    constructor(): this(
        profilePic = "",
        symbol = "",
        pricePerShare = 0.0,
        volume = 0,
        change = 0.0,
        marketCap = 0,
        region = "",
        currency = "",
        description = ""
    )

    class Builder {
        private var symbol: String = ""
        private var pricePerShare: Double = 0.0
        private var volume: Int = 0
        private var change: Double = 0.0
        private var marketCap: Int = 0
        private var region: String = ""
        private var currency: String = ""
        private var profilePic: String = ""
        private var description: String = ""

        fun symbol(symbol: String) = apply { this.symbol = symbol }
        fun pricePerShare(pricePerShare: Double) = apply { this.pricePerShare = pricePerShare }
        fun volume(volume: Int) = apply { this.volume = volume }
        fun change(change: Double) = apply { this.change = change }
        fun marketCap(marketCap: Int) = apply { this.marketCap = marketCap }
        fun region(region: String) = apply { this.region = region }
        fun currency(currency: String) = apply { this.currency = currency }
        fun profilePic(profilePic: String) = apply { this.profilePic = profilePic }
        fun description(description: String) = apply { this.description = description }

        fun build() = Stock(
            profilePic = profilePic,
            symbol = symbol,
            pricePerShare = pricePerShare,
            volume = volume,
            change = change,
            marketCap = marketCap,
            region = region,
            currency = currency,
            description = description
        )
    }
}


