package dev.lordyorden.tradely.ui.stock_info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.databinding.FragmentHostBinding
import dev.lordyorden.tradely.ui.explore.ExploreStocksViewModel

class HostFragment : Fragment() {

    private lateinit var binding: FragmentHostBinding
    private lateinit var child: Fragment
    private val defaultFragment: Fragment = StockInfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHostBinding.inflate(layoutInflater)
        setFragment(defaultFragment)
        return binding.root
    }

    fun setFragment(child: Fragment){
        this.child = child

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.host_FL_host, child)
            .commit()
    }
}