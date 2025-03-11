package dev.lordyorden.tradely.ui.stock_info.buy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.slider.RangeSlider
import dev.lordyorden.tradely.adapter.ItemLoadingHelper
import dev.lordyorden.tradely.databinding.FragmentBuyStockBinding
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.ui.stock_info.host.StockViewModel

class BuyStockFragment : Fragment() {

    private val viewModel: BuyStockViewModel by viewModels()
    private val stockViewModel: StockViewModel by activityViewModels()
    private lateinit var binding: FragmentBuyStockBinding
    private var amountOfShares: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBuyStockBinding.inflate(layoutInflater)

        stockViewModel.selectedStock.observe(viewLifecycleOwner) { stock ->
            ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.buyLBLTotalPrice, 0.0, stock.currency, "")
            ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.buyBTNPay, 0.0, stock.currency, "Buy ")
            viewModel.setStockInfo(stock)
        }

        initViews()

        return binding.root
    }

    private fun initViews() {
        binding.buySLDAmount.addOnChangeListener(RangeSlider.OnChangeListener { _, value, _ ->
            binding.buyLBLNumShares.text = buildString { append(value) }
            calcPriceAndDisplay(value)
        })

        binding.buyBTNPay.setOnClickListener{
            if(amountOfShares == 0.0f || viewModel.currentStock == null)
                return@setOnClickListener

            val stock = viewModel.currentStock!!
            ProfileManager.getInstance().buyStock(stock.symbol, amountOfShares.toDouble())
        }
    }

    private fun calcPriceAndDisplay(amountOfShares: Float) {
        viewModel.currentStock ?: return
        val stock = viewModel.currentStock!!

        this.amountOfShares = amountOfShares
        val price = stock.pricePerShare * amountOfShares
        ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.buyLBLTotalPrice, price, stock.currency, "")
        ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.buyBTNPay, price, stock.currency, "Buy ")

    }

    override fun onResume() {
        super.onResume()
        displayStock()
    }

    private fun displayStock() {
        viewModel.currentStock ?: return

        ItemLoadingHelper.renderNewStock(
            binding,
            viewModel.currentStock!!,
            binding.buyLBLSymbol,
            binding.buyLBLChange,
            binding.buyIMGChange,
            binding.buyLBLPrice,
            binding.buyIMGStock,
            binding.buyIMGFlag
        )
    }
}