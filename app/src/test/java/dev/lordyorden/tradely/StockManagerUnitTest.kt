package dev.lordyorden.tradely

import dev.lordyorden.tradely.interfaces.stock.StockChangesCallback
import dev.lordyorden.tradely.interfaces.stock.StockDB
import dev.lordyorden.tradely.interfaces.stock.StockFetchCallback
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class StockManagerUnitTest {

    private val stockManager = StockManager(object : StockDB {
        override fun updateStock(stock: Stock) {
            //pass
        }

        override fun getStock(symbol: String, fetchCallback: StockFetchCallback) {
            //pass
        }

        override fun registerStocks(callback: StockChangesCallback) {
            //pass
        }

        override fun saveStocks(stocks: List<Stock>) {
            //pass
        }

    })

    @Test
    fun country_code_parsing_with_data() {
        val resultJson = """{
                          "total_count": 5,
                          "results": [
                            {
                              "country_code": "US",
                              "name": "New York",
                              "cou_name_en": "United States"
                            },
                            {
                              "country_code": "CA",
                              "name": "Toronto",
                              "cou_name_en": "Canada"
                            },
                            {
                              "country_code": "FR",
                              "name": "Paris",
                              "cou_name_en": "France"
                            }
                          ]
                        }"""


        assertEquals(
            stockManager.getCountryCodeFromJson(resultJson, "United States", "New York"),
            "US"
        )
        assertEquals(stockManager.getCountryCodeFromJson(resultJson, "Canada", "Toronto"), "CA")
        assertEquals(stockManager.getCountryCodeFromJson(resultJson, "France", "Paris"), "FR")
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "Japan", "Tokyo"))
    }

    @Test
    fun country_code_parsing_with_empty() {
        val resultJson = """{
                          "total_count": 0,
                          "results": []
                        }"""


        assertNull(stockManager.getCountryCodeFromJson(resultJson, "United States", "New York"))
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "Canada", "Toronto"))
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "France", "Paris"))
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "Japan", "Tokyo"))
    }

    @Test
    fun country_code_parsing_with_empty_json() {
        val resultJson = "{}"

        assertNull(stockManager.getCountryCodeFromJson(resultJson, "United States", "New York"))
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "Canada", "Toronto"))
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "France", "Paris"))
        assertNull(stockManager.getCountryCodeFromJson(resultJson, "Japan", "Tokyo"))
    }

    @Test
    fun search_result_parsing_with_data() {
        val resJson = """{
                                       "bestMatches": [
                                           {
                                               "1. symbol": "AAPL",
                                               "2. name": "Apple Inc",
                                               "3. type": "Equity",
                                               "4. region": "United States",
                                               "5. marketOpen": "09:30",
                                               "6. marketClose": "16:00",
                                               "7. timezone": "UTC-04",
                                               "8. currency": "USD",
                                               "9. matchScore": "1.0000"
                                           },
                                           {
                                               "1. symbol": "F",
                                               "2. name": "Ford Motor Co",
                                               "3. type": "Equity",
                                               "4. region": "New York",
                                               "5. marketOpen": "09:30",
                                               "6. marketClose": "16:00",
                                               "7. timezone": "UTC-05",
                                               "8. currency": "USD",
                                               "9. matchScore": "0.7273"
                                           },
                                           {
                                               "1. symbol": "MCD",
                                               "2. name": "McDonald's corp",
                                               "3. type": "Equity",
                                               "4. region": "New York",
                                               "5. marketOpen": "10:00",
                                               "6. marketClose": "17:30",
                                               "7. timezone": "UTC-03",
                                               "8. currency": "USD",
                                               "9. matchScore": "1.000"
                                           },
                                           {
                                               "1. symbol": "NVDA",
                                               "2. name": "NVIDIA Corp",
                                               "3. type": "Equity",
                                               "4. region": "United States",
                                               "5. marketOpen": "09:15",
                                               "6. marketClose": "15:30",
                                               "7. timezone": "UTC+5.5",
                                               "8. currency": "USD",
                                               "9. matchScore": "0.4706"
                                           }
                                       ]
                                   }"""

        var results = stockManager.parseSearchResults("nvidia", resJson)
        assert(results.isNotEmpty())

        results.forEach { res ->
            assertEquals(res.symbol, "NVDA")
            assertEquals(res.region, "United States")
            assertEquals(res.currency, "USD")
            assertEquals(res.description, "NVIDIA Corp")
        }

        results = stockManager.parseSearchResults("McDonald's", resJson)
        assert(results.isNotEmpty())

        results.forEach { res ->
            assertEquals(res.symbol, "MCD")
            assertEquals(res.region, "New York")
            assertEquals(res.currency, "USD")
            assertEquals(res.description, "McDonald's corp")
        }

        results = stockManager.parseSearchResults("Toyota", resJson)
        assert(results.isEmpty())
    }

    @Test
    fun search_result_parsing_with_empty() {
        val resJson = """{
    "bestMatches": []
}"""

        var results = stockManager.parseSearchResults("nvidia", resJson)
        assert(results.isEmpty())

        results = stockManager.parseSearchResults("McDonald's", resJson)
        assert(results.isEmpty())
    }

    @Test
    fun search_result_parsing_with_empty_json() {
        val resJson = "{}"

        var results = stockManager.parseSearchResults("nvidia", resJson)
        assert(results.isEmpty())

        results = stockManager.parseSearchResults("McDonald's", resJson)
        assert(results.isEmpty())
    }

    @Test
    fun parseAndAddPrice_with_data() {
        val resJson = """{
    "Meta Data": {
        "1. Information": "Daily Prices (open, high, low, close) and Volumes",
        "2. Symbol": "NVDA",
        "3. Last Refreshed": "2025-02-28",
        "4. Output Size": "Compact",
        "5. Time Zone": "US/Eastern"
    },
    "Time Series (Daily)": {
        "2025-02-28": {
            "1. open": "118.0200",
            "2. high": "125.0900",
            "3. low": "116.4000",
            "4. close": "124.9200",
            "5. volume": "389091145"
        },
        "2025-02-27": {
            "1. open": "135.0000",
            "2. high": "135.0100",
            "3. low": "120.0100",
            "4. close": "120.1500",
            "5. volume": "443175846"
        },
        "2025-02-26": {
            "1. open": "129.9850",
            "2. high": "133.7300",
            "3. low": "128.4900",
            "4. close": "131.2800",
            "5. volume": "322553814"
        }
    }
}"""

        val stock = stockManager.parseAndAddPrice(
            stock = Stock.Builder()
                .symbol("NVDA")
                .build(),
            resJson
        )

        assertNotNull(stock)

        stock?.let {
            assert(stock.change == 1.0584646670055924)
            assert(stock.pricePerShare == 124.92)
            assertEquals(stock.marketCap, 2147483647)
            assertEquals(stock.volume, 389091145)
        }
    }

    @Test
    fun parseAndAddPrice_with_empty_stock_price_data() {
        val resJson = """{
    "Meta Data": {
        "1. Information": "Daily Prices (open, high, low, close) and Volumes",
        "2. Symbol": "NVDA",
        "3. Last Refreshed": "2025-02-28",
        "4. Output Size": "Compact",
        "5. Time Zone": "US/Eastern"
    },
    "Time Series (Daily)": {
        
    }
}"""
        parseAndAddPrice_assert(resJson)
    }

    @Test
    fun parseAndAddPrice_with_empty_metadata() {
        val resJson = """{
    "Meta Data": {
    },
    "Time Series (Daily)": {
        
    }
}"""

        parseAndAddPrice_assert_null(resJson)
    }

    @Test
    fun parseAndAddPrice_with_empty_json() {
        val resJson = "{}"

        parseAndAddPrice_assert_null(resJson)
    }

    @Test
    fun parseAndAddPrice_with_empty() {
        val resJson = ""

        parseAndAddPrice_assert_null(resJson)
    }

    private fun parseAndAddPrice_assert_null(resJson: String) {
        val stock = stockManager.parseAndAddPrice(
            stock = Stock.Builder()
                .symbol("NVDA")
                .build(),
            resJson
        )

        assertNull(stock)
    }

    private fun parseAndAddPrice_assert(resJson: String) {
        val stock = stockManager.parseAndAddPrice(
            stock = Stock.Builder()
                .symbol("NVDA")
                .build(),
            resJson
        )

        assertNotNull(stock)

        stock?.let {
            assert(stock.change == 0.0)
            assert(stock.pricePerShare == 0.0)
            assertEquals(stock.marketCap, 0)
            assertEquals(stock.volume, 0)
        }
    }
}