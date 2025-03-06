package dev.lordyorden.tradely.ui.stock_info

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DateTimeAxisFormater(private val baseDate: LocalDateTime, private val format: String = "yy-MM-dd HH:mm:ss") : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return floatToDate(value, format)
    }

    private fun normalizedFloatToDateTime(value: Float): LocalDateTime {
        return baseDate.plusHours(value.toLong())
    }

    private fun floatToDate(value: Float, format: String): String {
        val localDateTime = normalizedFloatToDateTime(value)
        val formatter = DateTimeFormatter.ofPattern(format)
        return localDateTime.format(formatter)
    }
}