package dev.lordyorden.tradely.ui.stock_info.buy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dev.lordyorden.tradely.adapter.ItemLoadingHelper
import dev.lordyorden.tradely.databinding.FragmentBuyStockBinding
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.ui.stock_info.host.StockViewModel

class BuyStockFragment : Fragment() {

    private val viewModel: BuyStockViewModel by viewModels()
    private val stockViewModel: StockViewModel by activityViewModels()
    private lateinit var binding: FragmentBuyStockBinding
    private var amountOfShares: Float = 0.0f
    private var oldAmountOfShares: Float = 0.0f
    private val isBuy: Boolean
        get() = oldAmountOfShares <= amountOfShares

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
        binding.buySLDAmount.addOnChangeListener{ _, value, _ ->
            binding.buyLBLNumShares.text = buildString { append(value) }
            calcPriceAndDisplay(value)
        }

        binding.buyBTNPay.setOnClickListener{
            if(viewModel.currentStock == null)
                return@setOnClickListener

            val stock = viewModel.currentStock!!

            if (isBuy)
                ProfileManager.getInstance().buyStock(stock.symbol, (amountOfShares - oldAmountOfShares).toDouble())
            else
                ProfileManager.getInstance().sellStock(stock.symbol, (oldAmountOfShares - amountOfShares).toDouble())
        }
    }

    private fun calcPriceAndDisplay(amountOfShares: Float): Boolean {
        viewModel.currentStock ?: return false
        val stock = viewModel.currentStock!!

        this.amountOfShares = amountOfShares


        val label = if(isBuy) "Buy " else "Sell "
        val diff = if(isBuy) {amountOfShares - oldAmountOfShares} else {oldAmountOfShares - amountOfShares}
        val price = stock.pricePerShare * diff

        ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.buyLBLTotalPrice, price, stock.currency, "")
        ItemLoadingHelper.updatePriceWithRegionalCurrency(binding.buyBTNPay, price, stock.currency, label)
        return isBuy
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

        viewModel.currentStock?.let {
            oldAmountOfShares = ProfileManager.getInstance().getAmountOwned(it.symbol).toFloat()
            binding.buySLDAmount.value = oldAmountOfShares
        }
    }
}