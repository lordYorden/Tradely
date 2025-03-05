package dev.lordyorden.tradely.ui.stock_info

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DateAxisFormater constructor(private val baseDate: LocalDate, private val format: String = "yy-MM-dd") : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return floatToDate(value, format)
    }

    private fun normalizedFloatToDate(value: Float): LocalDate {
        return baseDate.plusDays(value.toLong())
    }

    private fun floatToDate(value: Float, format: String): String {
        val localDate = normalizedFloatToDate(value)
        val formatter = DateTimeFormatter.ofPattern(format)
        return localDate.format(formatter)
    }
}