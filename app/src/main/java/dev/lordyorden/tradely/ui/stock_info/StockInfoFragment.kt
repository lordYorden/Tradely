package dev.lordyorden.tradely.ui.stock_info

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.adapter.ItemLoadingHelper
import dev.lordyorden.tradely.databinding.FragmentStockInfoBinding
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockParser
import dev.lordyorden.tradely.ui.home.HomeFragment
import dev.lordyorden.tradely.utilities.ImageLoader
import java.util.Locale


class StockInfoFragment : Fragment() {
    private lateinit var binding: FragmentStockInfoBinding
    private lateinit var chart: CandleStickChart
    private val viewM: StockInfoViewModel by viewModels()
    private lateinit var dataSet: CandleDataSet
    private lateinit var valueFormatter: ValueFormatter
    private val stockVM: StockViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataSet()
    }

    private fun initDataSet() {
        val entries: MutableList<CandleEntry> = mutableListOf()
        dataSet = CandleDataSet(entries, "stock")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockInfoBinding.inflate(inflater, container, false)
        chart = binding.infoCHARTCandle
        valueFormatter = DateAxisFormater(StockParser.baseDate)
        initChart()

        binding.infoBTNBuy.setOnClickListener{
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.host_FL_host, HomeFragment())
                .commit()
        }

        stockVM.selectedStock.observe(viewLifecycleOwner){ stock->
            viewM.setStockInfo(stock)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        renderNewStock()
    }

    private fun renderNewStock() {
        viewM.currentStock ?: return

        val currentStock:Stock = viewM.currentStock!!

        binding.infoLBLSymbol.text = currentStock.symbol
        binding.infoLBLChange.text = String.format(Locale.getDefault(), "%.2f%%", currentStock.change)
        ItemLoadingHelper.setChangeColorAndArrow(binding, binding.infoLBLChange, binding.infoIMGChange, currentStock.change)

        ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.infoLBLPrice, currentStock.pricePerShare, currentStock.currency)
//        binding.infoLBLPrice.text = buildString {
//            append("Price: ")
//            append(currentStock.pricePerShare)
//            append("$")
//        }

        val symbol = currentStock.symbol
        ImageLoader.getInstance().loadImage("https://assets.parqet.com/logos/symbol/$symbol?format=jpg", binding.infoIMGStock)
        ItemLoadingHelper.loadFlag(currentStock.region, binding.infoIMGFlag)

        presentMonthlyData()
    }

    private fun presentMonthlyData(){
        val entries = viewM.monthly
        setNewEntries(entries)
    }

    private fun initChart() {
        configChart()
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                h?.let {high ->

                    viewM.currentStock?.currency?.let { curr ->
                        ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.infoLBLPrice, high.y.toDouble(),
                            curr
                        )
                    }

//                    binding.infoLBLPrice.text = buildString {
//                        append("Price: ")
//                        append(it.y)
//                        append("$")
//                    }

                    binding.infoLBLDate.text = buildString {
                        append("Date: ")
                        append(valueFormatter.getFormattedValue(high.x))
                    }
                }
            }

            override fun onNothingSelected() {
                //pass
            }

        })
        binding.infoTBGScale.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.info_BTN_month -> {
                        val entries = viewM.daily
                        setNewEntries(entries, "MM-dd")
                    }

                    R.id.info_BTN_year -> {
                        val entries = viewM.weekly
                        setNewEntries(entries, "MM-dd")
                    }

                    R.id.info_BTN_max -> {
                        presentMonthlyData()
                    }

                    R.id.info_BTN_day -> {
                        val entries = viewM.hourly
                        setNewEntries(entries, "dd")
                    }
                    else -> {
                        presentMonthlyData()
                    }
                }
            }
        }
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

        if (entries.isEmpty()) {
            chart.clear()
            return
        }

        if(dataSet.entryCount > 0)
            dataSet.clear()

        entries.forEach{ entry ->
            dataSet.addEntry(entry)
        }

        val data = CandleData(dataSet)
        chart.data = data

        valueFormatter = DateAxisFormater(StockParser.baseDate, format)
        chart.xAxis.valueFormatter = valueFormatter
        chart.notifyDataSetChanged()
        chart.invalidate()
        chart.xAxis.axisMinimum = entries.first().x
        chart.xAxis.axisMaximum = entries.last().x
        chart.moveViewToX(entries.last().x)
        chart.fitScreen()
}

}