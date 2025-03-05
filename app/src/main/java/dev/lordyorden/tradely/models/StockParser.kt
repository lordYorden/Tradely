package dev.lordyorden.tradely.models

import android.util.Log
import com.github.mikephil.charting.data.CandleEntry
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StockParser {

    val baseDate: LocalDate = LocalDate.of(2020, 1, 1)

    private fun dateToFloat(localDate: LocalDate): Float {
        return dateToNormalizedFloat(localDate)
    }

    private fun dateStringToDate(date: String, format: String = "yyyy-MM-dd"): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(format)
        val localDate = LocalDate.parse(date, formatter)
        return localDate
    }

    private fun dateToNormalizedFloat(date: LocalDate, minDate: LocalDate = baseDate): Float {
        return (date.toEpochDay() - minDate.toEpochDay()).toFloat()
    }

    private fun parseMetaData(obj: JsonObject): String? {
        try {
            if (obj.has("Error Message")) {
                return null
            }

            if (obj.has("Meta Data")) {
                val metadata = obj.get("Meta Data").asJsonObject
                val currDate = metadata.get("3. Last Refreshed").asString
                return currDate
            }
        } catch (e: IllegalStateException) {
            e.message?.let { Log.e("MetaData parsing", it) }
        } catch (e: NullPointerException) {
            e.message?.let { Log.e("MetaData parsing", it) }
        }
        return null
    }

    private fun parseTimeSeries(obj: JsonObject, type: String, minDate: LocalDate = baseDate): List<CandleEntry>? {

        val entries: MutableList<CandleEntry> = mutableListOf()
        try {
            if (obj.has(type)) {
                val series = obj.get(type).asJsonObject

                series.asMap().forEach{(key, time) ->
                    val price = time.asJsonObject
                    val open = price.get("1. open").asFloat
                    val high = price.get("2. high").asFloat
                    val low = price.get("3. low").asFloat
                    val close = price.get("4. close").asFloat
                    val date = dateStringToDate(key)

                    if(date >= minDate){
                        val dateFloat = dateToFloat(date)
                        entries.add(CandleEntry(dateFloat, high, low, open , close))
                    }
                }
                return entries.reversed()
            }
        } catch (e: IllegalStateException) {
            e.message?.let { Log.e("MetaData parsing", it) }
        } catch (e: NullPointerException) {
            e.message?.let { Log.e("MetaData parsing", it) }
        }
        return null
    }

    fun parseWeeklyData(toParse: String): List<CandleEntry>? {
        try {
            val weekly = JsonParser.parseString(toParse).asJsonObject
            val currDate = parseMetaData(weekly)
            currDate?.let {
                val maxDate = dateStringToDate(currDate)
                val minDate = maxDate.minusYears(1)
                val entries = parseTimeSeries(weekly, "Weekly Time Series", minDate)
                return entries
            }

        } catch (e: IllegalStateException) {
            e.message?.let { Log.e("weekly data parsing", it) }
        }
        return null
    }

    fun parseDailyData(toParse: String): List<CandleEntry>? {
        try {
            val daily = JsonParser.parseString(toParse).asJsonObject
            val currDate = parseMetaData(daily)

            currDate?.let {
                val maxDate = dateStringToDate(currDate)
                val minDate = maxDate.minusMonths(1)
                val entries = parseTimeSeries(daily, "Time Series (Daily)", minDate)
                return entries
            }

        } catch (e: IllegalStateException) {
            e.message?.let { Log.e("daily data parsing", it) }
        }
        return null
    }

    fun parseMonthlyData(toParse: String): List<CandleEntry>? {
        try {
            val daily = JsonParser.parseString(toParse).asJsonObject
            val currDate = parseMetaData(daily)

            currDate?.let {
                val maxDate = dateStringToDate(currDate)
                val minDate = maxDate.minusYears(5)
                val entries = parseTimeSeries(daily, "Monthly Time Series", minDate)
                return entries
            }

        } catch (e: IllegalStateException) {
            e.message?.let { Log.e("monthly data parsing", it) }
        }
        return null
    }
}