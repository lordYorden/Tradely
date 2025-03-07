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
import dev.lordyorden.tradely.ui.stock_info.host.StockViewModel

class BuyStockFragment : Fragment() {

    private val viewModel: BuyStockViewModel by viewModels()
    private val stockViewModel: StockViewModel by activityViewModels()
    private lateinit var binding: FragmentBuyStockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBuyStockBinding.inflate(layoutInflater)

        stockViewModel.selectedStock.observe(viewLifecycleOwner) { stock ->
            viewModel.setStockInfo(stock)
        }

        return binding.root
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