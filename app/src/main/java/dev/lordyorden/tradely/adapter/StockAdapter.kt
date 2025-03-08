package dev.lordyorden.tradely.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.lordyorden.tradely.databinding.StockItemBinding
import dev.lordyorden.tradely.interfaces.stock.StockCallback
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.utilities.ImageLoader
import java.util.Locale

class StockAdapter(
    var stocks: List<Stock> = listOf(
        Stock.Builder()
            .symbol("No data Yet...")
            .build()
    )
) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    var stockCallback: StockCallback? = null
    var hasExtraOption = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = StockItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    private fun getItem(position: Int) = stocks[position]

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        with(holder) {

            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            holder.itemView.layoutParams = ItemLoadingHelper.fixedLastMargin(params, position, stocks.size)

            with(getItem(position)) {

                if (hasExtraOption) {
                    binding.stockRLEdit.visibility = View.VISIBLE
                } else{
                    binding.stockRLEdit.visibility = View.GONE
                }

                binding.stockLBLName.text = symbol
                binding.stockLBLDescription.text = description
                binding.stockLBLMarketCap.text = buildString {
                    append("Market Cap: ")
                    append(ItemLoadingHelper.formatBigNumberToString(marketCap.toDouble()))
                    append("$")
                }

                ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.stockLBLPrice, pricePerShare, currency)

                binding.stockLBLChange.text = String.format(Locale.getDefault(), "%.2f%%", change)

                ImageLoader.getInstance().loadImage("https://assets.parqet.com/logos/symbol/$symbol?format=jpg", binding.stockIMGStock)
                /*ImageLoader.getInstance().loadImage(profilePic, binding.stockIMGStock)*/

                ItemLoadingHelper.loadFlag(region, binding.stockIMGFlag)

                ItemLoadingHelper.setChangeColorAndArrow(binding, binding.stockLBLChange, binding.stockIMGChange, change)
            }
        }
    }


    inner class StockViewHolder(val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.stockRLEdit.setOnClickListener {
                stockCallback?.onExtraClicked(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }

            binding.stockLBLName.setOnClickListener {
                stockCallback?.onStockClicked(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }
    }


}
