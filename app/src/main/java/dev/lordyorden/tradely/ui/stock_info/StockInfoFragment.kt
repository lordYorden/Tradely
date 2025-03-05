package dev.lordyorden.tradely.ui.stock_info

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.databinding.FragmentStockInfoBinding


class StockInfoFragment : Fragment() {

    private lateinit var binding: FragmentStockInfoBinding
    private lateinit var chart: CandleStickChart
    private lateinit var viewM: StockInfoViewModel
    private lateinit var dataSet: CandleDataSet
    private lateinit var valueFormatter: ValueFormatter

    private fun initDataSet() {
        val entries: MutableList<CandleEntry> = viewM.getEntriesMonthly().toMutableList()
        dataSet = CandleDataSet(entries, "stock")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockInfoBinding.inflate(inflater, container, false)
        chart = binding.infoCHARTCandle
        viewM = ViewModelProvider(this)[StockInfoViewModel::class.java]
        valueFormatter = DateAxisFormater(viewM.stockParser.baseDate)
        initDataSet()
        initChart()

        return binding.root
    }

    private fun initChart() {
        configChart()
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                h?.let {
                    binding.infoLBLPrice.text = buildString {
                        append("Price: ")
                        append(it.y)
                        append("$")
                    }

                    binding.infoLBLDate.text = buildString {
                        append("Date: ")
                        append(valueFormatter.getFormattedValue(it.x))
                    }
                }
            }

            override fun onNothingSelected() {
                binding.infoLBLPrice.text = buildString {
                    append("Price: 137.71\$")
                }
            }

        })
        binding.infoTBGScale.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.info_BTN_month -> {
                        val entries = viewM.getEntriesDaily()
                        setNewEntries(entries, "MM-dd")
                    }

                    R.id.info_BTN_year -> {
                        val entries = viewM.getEntriesWeekly()
                        setNewEntries(entries, "MM-dd")
                    }

                    R.id.info_BTN_max -> {
                        val entries = viewM.getEntriesMonthly()
                        setNewEntries(entries)
                    }
                    else -> {
                        val entries = viewM.getEntriesMonthly()
                        setNewEntries(entries)
                    }
                }
            }
        }
        val data = CandleData(dataSet)
        chart.data = data
        chart.invalidate()
        chart.fitScreen()
    }

    private fun configChart() {
        val textColor = resources.getColor(R.color.text_color, null)
        chart.description.textColor = textColor
        chart.legend.isEnabled = false
        chart.xAxis.textColor = textColor
        chart.xAxis.valueFormatter = valueFormatter
        chart.axisLeft.textColor = textColor
        chart.axisRight.isEnabled = false
        chart.isAutoScaleMinMaxEnabled = true
        chart.description.isEnabled = false

        // Created a CandleDataSet from the entries
        dataSet.valueTextColor = textColor
        dataSet.setDrawIcons(false)
        dataSet.increasingColor = Color.GREEN // Color for up (green) candlesticks
        dataSet.increasingPaintStyle =
            Paint.Style.FILL // Set the paint style to Fill for green candlesticks
        dataSet.decreasingColor = Color.RED // Color for down (red) candlesticks
        dataSet.shadowColorSameAsCandle =
            true // Using the same color for shadows as the candlesticks
        dataSet.setDrawValues(false) // Hiding the values on the chart if not needed
    }

    private fun setNewEntries(entries: List<CandleEntry>, format: String = "yy-MM-dd"){
        dataSet.clear()
        entries.forEach{ entry ->
            dataSet.addEntry(entry)
        }

        valueFormatter = DateAxisFormater(viewM.stockParser.baseDate, format)
        chart.xAxis.valueFormatter = valueFormatter
        chart.notifyDataSetChanged()
        chart.invalidate()
        chart.xAxis.axisMinimum = entries.first().x
        chart.xAxis.axisMaximum = entries.last().x
        chart.moveViewToX(entries.last().x)
        chart.fitScreen()
    }

}