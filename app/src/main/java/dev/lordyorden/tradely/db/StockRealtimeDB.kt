package dev.lordyorden.tradely.db

import android.util.Log
import com.github.mikephil.charting.data.CandleEntry
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import dev.lordyorden.tradely.interfaces.stock.StockChangesCallback
import dev.lordyorden.tradely.interfaces.stock.StockDB
import dev.lordyorden.tradely.interfaces.stock.StockFetchCallback
import dev.lordyorden.tradely.interfaces.stock.StockPriceUpdateCallback
import dev.lordyorden.tradely.models.PriceEntry
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.utilities.Constants

class StockRealtimeDB : StockDB {

    private val db: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference = db.getReference(Constants.DB.STOCKS_REF)

    override fun updateStock(stock: Stock) {
        val stockRef = ref.child(stock.symbol)
        stockRef.setValue(stock)
            .addOnFailureListener { e ->
                Log.e(
                    "Update stock",
                    "failed to update ${stock.symbol} with error: ${e.localizedMessage}"
                )
            }
    }

    override fun updateStockPrices(symbol: String, prices: List<CandleEntry>, type: String) {
        val stockRef = ref.child(symbol).child(Constants.DB.PRICES_REF).child(type)
        stockRef.setValue(prices)
            .addOnFailureListener { e ->
                Log.e(
                    "Update stock prices",
                    "failed to update $symbol with error: ${e.localizedMessage}"
                )
            }
    }

    override fun registerUpdateOnPrice(symbol: String, callback: StockPriceUpdateCallback) {
        ref.child(symbol).child(Constants.DB.PRICES_REF)
            .addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val prices = snapshot.getValue<List<PriceEntry>>()
                    val type = snapshot.key

                    prices?.let {
                        type?.let {
                            callback.onPricesUpdate(prices, type)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val prices = snapshot.getValue<List<CandleEntry>>()
                    val type = snapshot.key

                    prices?.let {
                        type?.let {
                            callback.onPricesUpdate(prices, type)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    //pass
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //pass
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("stock price", "failed to fetch, error: ${error.toException()}")
                }

            })
    }

    override fun getStock(symbol: String, fetchCallback: StockFetchCallback) {
        val stockRef = ref.child(symbol)
        stockRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stock = snapshot.getValue<Stock>()
                stock?.let {
                    fetchCallback.onStockFetch(it)
                } ?: {
                    fetchCallback.onStockFetchFailed()
                    Log.e("fetch stock", "failed to fetch $symbol")
                }


            }

            override fun onCancelled(error: DatabaseError) {
                fetchCallback.onStockFetchFailed()
                Log.e("fetch stock", "failed to fetch $symbol with error: ${error.toException()}")
            }

        })
    }

    override fun registerStocks(callback: StockChangesCallback) {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val stock = snapshot.getValue<Stock>()
                stock?.let {
                    Log.d("stock listener", "Added: ${stock.symbol}")
                    callback.onStockAdded(stock)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val stock = snapshot.getValue<Stock>()
                stock?.let {
                    Log.d("stock listener", "Changed: ${stock.symbol}")
                    callback.onStockChanged(stock)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val stock = snapshot.getValue<Stock>()
                stock?.let {
                    Log.d("stock listener", "removed: ${stock.symbol}")
                    callback.onStockRemoved(stock.symbol)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //pass
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("stock listener", "failed to fetch, error: ${error.toException()}")
            }

        })
    }

    override fun saveStocks(stocks: List<Stock>) {
        stocks.forEach {
            updateStock(it)
        }
    }
}