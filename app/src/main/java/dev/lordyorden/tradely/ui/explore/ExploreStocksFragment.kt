package dev.lordyorden.tradely.ui.explore

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.lordyorden.tradely.adapter.StockAdapter
import dev.lordyorden.tradely.databinding.FragmentExploreStocksBinding
import dev.lordyorden.tradely.interfaces.stock.StockCallback
import dev.lordyorden.tradely.models.Stock
import dev.lordyorden.tradely.ui.stock_info.StockViewModel

class ExploreStocksFragment : Fragment() {

    private var _binding: FragmentExploreStocksBinding? = null
    private lateinit var adapter: StockAdapter
    private val stockVM: StockViewModel by activityViewModels()

    private val viewModel: ExploreStocksViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreStocksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = StockAdapter()
        binding.stocksRVStocks.adapter = adapter
        binding.stocksRVStocks.layoutManager = LinearLayoutManager(context)
        adapter.stockCallback  = object : StockCallback {
            override fun onEditClicked(stock: Stock, position: Int) {
                Toast.makeText(context, "Edit clicked on ${stock.symbol}", Toast.LENGTH_SHORT).show()
                stockVM.selectStock(stock)
            }
        }

        binding.stocksSVSearch.setOnCloseListener {
            viewModel.stopSearch()
            false
        }

        binding.stocksSVSearch.setOnQueryTextListener(object :OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.deepSearch(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchStocks(it)
                    return true
                }
                return false
            }

        })

        viewModel.stocks.observe(viewLifecycleOwner) { stocks ->
            adapter.stocks = stocks
            adapter.notifyDataSetChanged()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}