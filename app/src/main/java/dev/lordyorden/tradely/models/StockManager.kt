package dev.lordyorden.tradely.models

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.ref.WeakReference

class StockManager private constructor(context: Context){

    companion object {

        @Volatile
        private var instance: StockManager? = null

        fun getInstance(): StockManager {
            return instance
                ?: throw IllegalStateException("StockManager must be initialized by calling init(context) before use")
        }

        fun init(context: Context): StockManager {
            return instance ?: synchronized(this) {
                instance ?: StockManager(context).also { instance = it }
            }
        }
    }

    private val contextRef = WeakReference(context)
    private val db = Firebase.firestore

    var stocks = generateStocks()
        private set

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
                .build())

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
                .build())

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
                .build())

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
                .build())

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
                .build())

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
                .build())

        return stocks
    }
}