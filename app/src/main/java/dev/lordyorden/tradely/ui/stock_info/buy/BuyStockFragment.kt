package dev.lordyorden.tradely.ui.stock_info.buy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.lordyorden.tradely.databinding.FragmentBuyStockBinding

class BuyStockFragment : Fragment() {

    companion object {
        fun newInstance() = BuyStockFragment()
    }

    private val viewModel: BuyStockViewModel by viewModels()
    private lateinit var binding: FragmentBuyStockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBuyStockBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    private fun initViews() {
        //TODO
    }
}