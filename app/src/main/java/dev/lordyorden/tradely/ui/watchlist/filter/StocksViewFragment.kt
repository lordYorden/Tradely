package dev.lordyorden.tradely.ui.watchlist.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.lordyorden.tradely.R

import dev.lordyorden.tradely.adapter.StockAdapter
import dev.lordyorden.tradely.databinding.FragmentStocksViewBinding
import dev.lordyorden.tradely.interfaces.stock.StockCallback
import dev.lordyorden.tradely.interfaces.stock.StockUpdateCallback
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.models.StockManager
import dev.lordyorden.tradely.ui.home.profile_card.ProfileViewModel
import dev.lordyorden.tradely.ui.stock_info.host.StockViewModel


abstract class StocksViewFragment() : Fragment() {

    protected lateinit var binding: FragmentStocksViewBinding
    private lateinit var adapter: StockAdapter
    private val stockVM: StockViewModel by activityViewModels()
    private val profileSelector: ProfileViewModel by activityViewModels()

    abstract fun getViewMode(): FilterStocksViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStocksViewBinding.inflate(layoutInflater, container , false)

        adapter = StockAdapter()
        adapter.hasExtraOption = false
        binding.stocksRVStocks.adapter = adapter
        binding.stocksRVStocks.layoutManager = LinearLayoutManager(context)

        StockManager.getInstance().registerObserver(object: StockUpdateCallback {
            override fun updateStocks() {
                getViewMode().onStockChange()
            }
        })

        getViewMode().getStocksLive().observe(viewLifecycleOwner) { stocks ->
            if(stocks != null) {
                adapter.stocks = stocks
                adapter.notifyDataSetChanged()
            }
        }

        profileSelector.selectedProfile.observe(viewLifecycleOwner) {
            getViewMode().onProfileChanged(it)
        }

        adapter.stockCallback  = object : StockCallback {
            override fun onExtraClicked(stock: Stock, position: Int) {
                //pass
            }

            override fun onStockClicked(stock: Stock, position: Int) {
                stockVM.selectStock(stock)
                val navController = findNavController()
                navController.navigate(R.id.action_home_to_stock_info)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }
}