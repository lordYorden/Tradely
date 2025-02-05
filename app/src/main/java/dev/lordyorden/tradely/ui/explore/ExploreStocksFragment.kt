package dev.lordyorden.tradely.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.lordyorden.tradely.adapter.StockAdapter
import dev.lordyorden.tradely.databinding.FragmentExploreStocksBinding
import dev.lordyorden.tradely.interfaces.StockCallback
import dev.lordyorden.tradely.models.Stock

class ExploreStocksFragment : Fragment() {

    private var _binding: FragmentExploreStocksBinding? = null
    private lateinit var adapter: StockAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(ExploreStocksViewModel::class.java)

        _binding = FragmentExploreStocksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = StockAdapter()
        binding.stocksRVStocks.adapter = adapter
        binding.stocksRVStocks.layoutManager = LinearLayoutManager(context)
        adapter.stockCallback  = object : StockCallback {
            override fun onEditClicked(stock: Stock, position: Int) {
                Toast.makeText(context, "Edit clicked on ${stock.symbol}", Toast.LENGTH_SHORT).show()
            }
        }

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