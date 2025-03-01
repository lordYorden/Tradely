package dev.lordyorden.tradely.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

import dev.lordyorden.tradely.R

object ItemLoadingHelper {
    fun setChangeColorAndArrow(binding: ViewBinding, label: TextView, icon: ImageView,  change: Double) {
        val positiveColor = binding.root.resources.getColor(R.color.positive, null)
        val negativeColor = binding.root.resources.getColor(R.color.negative, null)

        val arrowUp: Int = R.drawable.ic_arrow_up
        val arrowDown: Int = R.drawable.ic_arrow_down

        if(change > 0){
            label.setTextColor(positiveColor)
            icon.setImageResource(arrowUp)
            icon.setColorFilter(positiveColor)
        } else {
            label.setTextColor(negativeColor)
            icon.setImageResource(arrowDown)
            icon.setColorFilter(negativeColor)
        }
    }

    fun formatBigNumberToString(num: Double) : String {
        return when {
            num >= 1_000_000_000 -> "${checkAndFormatIfInt(num, 1_000_000_000)}B"
            num >= 1_000_000 -> "${checkAndFormatIfInt(num, 1_000_000)}M"
            num >= 1_000 -> "${checkAndFormatIfInt(num, 1_000)}K"
            else -> num.toString()
        }
    }

    private fun checkAndFormatIfInt(amount: Double, divisor: Int): String {
        val formatedAmount = (amount / divisor)
        return if (amount % divisor == 0.0) {
            formatedAmount.toInt().toString()
        } else {
            formatedAmount.toString()
        }
    }

    fun fixedLastMargin(params: RecyclerView.LayoutParams, position: Int, size: Int): RecyclerView.LayoutParams{
        when(position){
            size - 1 -> {
                params.bottomMargin = 200 // last item bottom margin
            }
            else -> {
                params.bottomMargin = 0 // last item bottom margin
            }
        }
        return params
    }
}