package dev.lordyorden.tradely.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding

import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.databinding.ProfileItemBinding

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
}