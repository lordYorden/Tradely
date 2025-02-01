package dev.lordyorden.tradely.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.lordyorden.tradely.R
//import dev.lordyorden.test_fragments.utilities.Constants
import dev.lordyorden.tradely.databinding.ProfileItemBinding
import dev.lordyorden.tradely.databinding.StockItemBinding
import dev.lordyorden.tradely.interfaces.StockCallback
//import dev.lordyorden.test_fragments.interfaces.MovieCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.utilities.ImageLoader

class StockAdapter(
    var stocks: List<Stock> = listOf(
        Stock.Builder()
            .symbol("No data Yet...")
            .build()
    )
) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    var stockCallback: StockCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = StockItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    private fun getItem(position: Int) = stocks[position]

//    private fun setChangeColorAndArrow(binding: ProfileItemBinding, change: Double) {
//        val positiveColor = binding.root.resources.getColor(R.color.positive, null)
//        val negativeColor = binding.root.resources.getColor(R.color.negative, null)
//
//        val arrowUp: Int = R.drawable.ic_arrow_up
//        val arrowDown: Int = R.drawable.ic_arrow_down
//
//        if(change > 0){
//            binding.profileLBLChange.setTextColor(positiveColor)
//            binding.profileIMGChange.setImageResource(arrowUp)
//            binding.profileIMGChange.setColorFilter(positiveColor)
//        } else {
//            binding.profileLBLChange.setTextColor(negativeColor)
//            binding.profileIMGChange.setImageResource(arrowDown)
//            binding.profileIMGChange.setColorFilter(negativeColor)
//        }
//    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.stockLBLName.text = symbol
                binding.stockLBLDescription.text = description
                binding.stockLBLMarketCap.text = buildString {
                    append("Market Cap: ")
                    append(marketCap)
                    append("$")
                }

                binding.stockLBLChange.text = buildString {
                    append(change)
                    append("%")
                }

                ImageLoader.getInstance().loadImage(profilePic, binding.stockIMGStock)
                ItemLoadingHelper.setChangeColorAndArrow(binding, binding.stockLBLChange, binding.stockIMGChange, change)
            }
        }
    }


    inner class StockViewHolder(val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.stockRLEdit.setOnClickListener {
                stockCallback?.onEditClicked(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }
    }


}
