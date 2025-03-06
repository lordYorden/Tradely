package dev.lordyorden.tradely.models

import android.util.Log
import com.github.mikephil.charting.data.CandleEntry
import com.google.gson.JsonParser
import dev.lordyorden.tradely.BuildConfig
import dev.lordyorden.tradely.interfaces.stock.StockChangesCallback
import dev.lordyorden.tradely.interfaces.stock.StockDB
import dev.lordyorden.tradely.interfaces.stock.StockFetchCallback
import dev.lordyorden.tradely.interfaces.stock.StockPriceUpdateCallback
import dev.lordyorden.tradely.interfaces.stock.StockUpdateCallback
import dev.lordyorden.tradely.utilities.HttpRequestHandler
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class StockManager private constructor(private val db: StockDB) {

    companion object {

        @Volatile
        private var instance: StockManager? = null

        fun getInstance(): StockManager {
            return instance
                ?: throw IllegalStateException("StockManager must be initialized by calling init(db) before use")
        }

        fun init(db: StockDB): StockManager {
            return instance ?: synchronized(this) {
                instance ?: StockManager(db).also { instance = it }
            }
        }

    }

    val stocks: MutableList<Stock> = mutableListOf()

    private fun filterStocksBySymbol(symbol: String) {
        stocks.removeIf { stock -> stock.symbol == symbol }
    }

    fun registerStocks(callback: StockUpdateCallback) {
        db.registerStocks(object : StockChangesCallback {
            override fun onStockChanged(stock: Stock) {
                if (stocks.isNotEmpty()) {
                    filterStocksBySymbol(stock.symbol)
                }

                stocks.add(stock)
                callback.updateStocks()
            }

            override fun onStockRemoved(symbol: String) {
                if (stocks.isNotEmpty())
                    filterStocksBySymbol(symbol)
                callback.updateStocks()
            }

            override fun onStockAdded(stock: Stock) {
                stocks.add(stock)

                db.registerUpdateOnPrice(stock.symbol, object : StockPriceUpdateCallback{
                    override fun onPricesUpdate(prices: List<CandleEntry>, type: String) {
                        when(type){
                            "monthly" -> {
                                stock.monthly = prices.toMutableList()
                            }

                            "weekly" -> {
                                stock.weekly = prices.toMutableList()
                            }

                            "daily" -> {
                                stock.daily = prices.toMutableList()
                            }

                            else -> Log.e("price update", "Price update on stock ${stock.symbol} with invalid type $type")
                        }
                        callback.updateStocks()
                    }
                })
                callback.updateStocks()
            }

        })
    }

    fun uploadTestPrice(){
        StockParser.parseWeeklyData(TestDataProvider.getWeeklyData())
            ?.let { db.updateStockPrices("IBM", it, "weekly") }

        StockParser.parseMonthlyData(TestDataProvider.getMonthlyData())
            ?.let { db.updateStockPrices("IBM", it, "monthly") }

        StockParser.parseDailyData(TestDataProvider.getDailyData())
            ?.let { db.updateStockPrices("IBM", it, "daily") }
    }

    private fun generateStocks(): List<Stock> {
        val stocks = mutableListOf<Stock>()
        stocks.add(
            Stock.Builder()
                .symbol("AAPL")
                .pricePerShare(134.5)
                .volume(1000000)
                .change(0.5)
                .marketCap(2000000)
                .region("US")
                .currency("USD")
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/f/f1/Logo_Apple_Blanco.png")
                .description("Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software, and online services.")
                .build()
        )

        stocks.add(
            Stock.Builder()
                .symbol("GOOGL")
                .pricePerShare(2000.0)
                .volume(1000000)
                .change(0.5)
                .marketCap(2000000)
                .region("US")
                .currency("USD")
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/3/3a/Google-favicon-vector.png")
                .description("Alphabet Inc. is an American multinational conglomerate headquartered in Mountain View, California.")
                .build()
        )

        stocks.add(
            Stock.Builder()
                .symbol("AMZN")
                .pricePerShare(3000.0)
                .volume(1000000)
                .change(0.5)
                .marketCap(2000000)
                .region("US")
                .currency("USD")
                .description("Amazon.com, Inc. is an American multinational technology company based in Seattle, Washington.")
                .profilePic("https://upload.wikimedia.org/wikipedia/commons/3/32/Amazon_logo._CB635397845.png")
                .build()
        )

        stocks.add(
            Stock.Builder()
                .symbol("TSLA")
                .pricePerShare(600.0)
                .volume(1000000)
                .change(0.5)
                .marketCap(2000000)
                .region("US")
                .currency("USD")
                .profilePic("https://api-ninjas-data.s3.us-west-2.amazonaws.com/logos/lefd12553d6a4f7e57b3ac4f4927181d7a651d0d6.png")
                .description("Tesla, Inc. is an American electric vehicle and clean energy company based in Palo Alto, California.")
                .build()
        )

        stocks.add(
            Stock.Builder()
                .symbol("MSFT")
                .pricePerShare(200.0)
                .volume(1000000)
                .change(0.5)
                .marketCap(2000000)
                .region("US")
                .currency("USD")
                .profilePic("https://mailmeteor.com/logos/assets/PNG/Microsoft_Logo_256px.png")
                .description("Microsoft Corporation is an American multinational technology company with headquarters in Redmond, Washington.")
                .build()
        )

        stocks.add(
            Stock.Builder()
                .symbol("META")
                .pricePerShare(300.0)
                .volume(1000000)
                .change(0.5)
                .marketCap(2000000)
                .region("US")
                .currency("USD")
                .profilePic("https://assets.stickpng.com/images/61fae2d395e6ca00047b4f12.png")
                .description("Meta Platforms, Inc. is an American multinational technology conglomerate based in Menlo Park, California.")
                .build()
        )

        return stocks
    }

    fun getCountryCodeFromJson(json: String, state: String, city: String): String? {
        try {
            val obj = JsonParser.parseString(json).asJsonObject
            val results = obj.get("results").asJsonArray
            results.forEach { item ->
                val res = item.asJsonObject
                val code = res.get("country_code").asString
                val name = res.get("name").asString
                val stateName = res.get("cou_name_en").asString

                if (state.contains(stateName, ignoreCase = true) || name.contains(
                        city,
                        ignoreCase = true
                    )
                )
                    return code
            }
            return null

        } catch (e: IllegalStateException) {
            return null
        }catch (e: NullPointerException){
            return null
        }
    }

    private fun updateRegionToCountryCode(stock: Stock, region: String, param: StockFetchCallback) {
        var city = region
        var state = region

        if (region.contains("/")) {
            val stateCity = region.split("/")
            state = stateCity[0]
            city = stateCity[1]
        }

        val url =
            "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/geonames-all-cities-with-a-population-1000/records"
        val args = buildMap<String, String> {
            put("select", "country_code, name, cou_name_en")
            put("where", buildString {
                append("cou_name_en like")
                append('"')
                append(state)
                append('"')
                append(" or ")
                append('"')
                append(city)
                append('"')
                append(" in alternate_names")
            })
            put("limit", "3")
        }
        HttpRequestHandler.getInstance().get(url, args, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("http request", "failed to url $url")
            }

            override fun onResponse(call: Call, response: Response) {

                response.body?.let { body ->
                    val code = getCountryCodeFromJson(body.string(), state, city)

                    code?.let {
                        stock.region = it
                        param.onStockFetch(stock)
                    } ?: param.onStockFetchFailed()
                } ?: param.onStockFetchFailed()

            }

        })
    }

    fun searchStock(keyword: String, callback: StockFetchCallback) {

        val url = "https://www.alphavantage.co/query"
        val args = buildMap<String, String> {
            put("keywords", keyword)
            put("apikey", BuildConfig.STOCK_API_KEY)
            put("function", "SYMBOL_SEARCH")
        }

        HttpRequestHandler.getInstance().get(url, args, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onStockFetchFailed()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val results = parseSearchResults(keyword, it.string())
                    results.forEach { res ->
                        updateRegionToCountryCode(res, res.region, object : StockFetchCallback {
                            override fun onStockFetch(stock: Stock) {
                                updateStockPriceAndChange(stock, callback)
                            }

                            override fun onStockFetchFailed() {
                                Log.e("county code", "couldn't fetch country code")
                            }
                        })
                    }
                } ?: callback.onStockFetchFailed()
            }
        })
    }

    fun parseSearchResults(keyword: String, json: String): List<Stock> {

        val stocks: MutableList<Stock> = mutableListOf()

        try {
            val obj = JsonParser.parseString(json).asJsonObject
            val results = obj.get("bestMatches").asJsonArray

            results.forEach { item ->
                val res = item.asJsonObject
                val symbol = res.get("1. symbol").asString
                val description = res.get("2. name").asString
                val region = res.get("4. region").asString
                val currency = res.get("8. currency").asString

                if (description.contains(keyword, ignoreCase = true) || symbol.startsWith(
                        keyword,
                        ignoreCase = true
                    )
                ) {
                    val stockFromSearch = Stock.Builder()
                        .symbol(symbol)
                        .region(region)
                        .description(description)
                        .currency(currency)
                        .build()

                    stocks.add(stockFromSearch)
                }
            }
            return stocks

        } catch (e: IllegalStateException) {
            return listOf()
        }catch (e: NullPointerException) {
            return listOf()
        }
    }

    private fun updateStockPriceAndChange(stock: Stock, callback: StockFetchCallback) {
        val url = "https://www.alphavantage.co/query"
        val args = buildMap<String, String> {
            put("function", "TIME_SERIES_DAILY")
            put("symbol", stock.symbol)
            put("apikey", BuildConfig.STOCK_API_KEY)
        }

        HttpRequestHandler.getInstance().get(url, args, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onStockFetchFailed()
            }

            override fun onResponse(call: Call, response: Response) {

                response.body?.let {
                    val res = parseAndAddPrice(stock, it.string())
                    res?.let { s ->
                        callback.onStockFetch(s)
                    } ?: callback.onStockFetchFailed()
                } ?: callback.onStockFetchFailed()
            }
        })
    }

    fun parseAndAddPrice(stock: Stock, json: String): Stock? {
        try {
            val obj = JsonParser.parseString(json).asJsonObject
            if (obj.has("Error Message")) {
                return null
            }

            if (!obj.has("Meta Data")) {
                return null
            }

            val metadata = obj.get("Meta Data").asJsonObject
            val currDate = metadata.get("3. Last Refreshed").asString
            val prices = obj.get("Time Series (Daily)").asJsonObject

            if (prices.has(currDate)) {
                val res = prices.asJsonObject.get(currDate).asJsonObject
                val open = res.get("1. open").asDouble
                val close = res.get("4. close").asDouble
                val volume = res.get("5. volume").asInt


                val change = if (open != 0.0) close / open else 0.0
                val marketCap = close * volume

                stock.volume = volume
                stock.pricePerShare = close
                stock.change = change
                stock.marketCap = marketCap.toInt()
            }
        } catch (e: IllegalStateException) {
            return null
        }
        catch (e: NullPointerException) {
            return null
        }

        return stock
    }

}