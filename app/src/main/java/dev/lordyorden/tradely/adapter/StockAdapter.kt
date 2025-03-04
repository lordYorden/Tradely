package dev.lordyorden.tradely.adapter

//import dev.lordyorden.test_fragments.utilities.Constants
//import dev.lordyorden.test_fragments.interfaces.MovieCallback
import android.view.LayoutInflater
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

            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            holder.itemView.layoutParams = ItemLoadingHelper.fixedLastMargin(params, position, stocks.size)

            with(getItem(position)) {
                binding.stockLBLName.text = symbol
                binding.stockLBLDescription.text = description
                binding.stockLBLMarketCap.text = buildString {
                    append("Market Cap: ")
                    append(ItemLoadingHelper.formatBigNumberToString(marketCap.toDouble()))
                    append("$")
                }

                binding.stockLBLPrice.text = buildString {
                    append("Price: ")
                    append(pricePerShare)
                    append("$")
                }

                binding.stockLBLChange.text = String.format(Locale.getDefault(), "%.2f%%", change)

                ImageLoader.getInstance().loadImage("https://assets.parqet.com/logos/symbol/$symbol?format=jpg", binding.stockIMGStock)

                ItemLoadingHelper.loadFlag(region, binding.stockIMGFlag)

                /*ImageLoader.getInstance().loadImage(profilePic, binding.stockIMGStock)*/
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
