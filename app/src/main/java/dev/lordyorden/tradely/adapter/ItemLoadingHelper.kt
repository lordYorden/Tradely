package dev.lordyorden.tradely.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.utilities.ImageLoader
import java.util.Currency
import java.util.Locale

object ItemLoadingHelper {
    fun setChangeColorAndArrow(
        binding: ViewBinding,
        label: TextView,
        icon: ImageView,
        change: Double
    ) {
        val positiveColor = binding.root.resources.getColor(R.color.positive, null)
        val negativeColor = binding.root.resources.getColor(R.color.negative, null)

        val arrowUp: Int = R.drawable.ic_arrow_up
        val arrowDown: Int = R.drawable.ic_arrow_down

        if (change > 0) {
            label.setTextColor(positiveColor)
            icon.setImageResource(arrowUp)
            icon.setColorFilter(positiveColor)
        } else {
            label.setTextColor(negativeColor)
            icon.setImageResource(arrowDown)
            icon.setColorFilter(negativeColor)
        }
    }

    fun updatePriceWithRegionalCurrency(priceView: TextView, price: Double, currency: String) {
        var curr: Currency = Currency.getInstance("USD")

        try {
            curr = Currency.getInstance(currency)
        } catch (e: Exception) {
            //pass
        }

        priceView.text = buildString {
            append("price: ")
            append(String.format(Locale.getDefault(), "%.2f ", price))
            append(curr.symbol)
        }
    }

    fun formatBigNumberToString(num: Double): String {
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

    fun fixedLastMargin(
        params: RecyclerView.LayoutParams,
        position: Int,
        size: Int
    ): RecyclerView.LayoutParams {
        when (position) {
            size - 1 -> {
                params.bottomMargin = 400 // last item bottom margin
            }

            else -> {
                params.bottomMargin = 0 // last item bottom margin
            }
        }
        return params
    }

    fun loadFlag(country: String, imageView: AppCompatImageView) {
        if (!Profile.isValidCountry(country)) return

        val flagUrl = "https://flagsapi.com/$country/flat/64.png"
        ImageLoader.getInstance().loadImage(flagUrl, imageView, R.drawable.us)
    }

    fun renderNewStock(
        binding: ViewBinding,
        stock: Stock,
        symbolView: TextView,
        changeView: TextView,
        changeIcon: AppCompatImageView,
        priceView: TextView,
        symbolImageView: AppCompatImageView,
        flagView: AppCompatImageView
    ) {
        symbolView.text = stock.symbol
        changeView.text = String.format(Locale.getDefault(), "%.2f%%", stock.change)
        setChangeColorAndArrow(binding, changeView, changeIcon, stock.change)
        updatePriceWithRegionalCurrency(priceView, stock.pricePerShare, stock.currency)
        val symbol = stock.symbol
        ImageLoader.getInstance()
            .loadImage("https://assets.parqet.com/logos/symbol/$symbol?format=jpg", symbolImageView)
        loadFlag(stock.region, flagView)
    }
}