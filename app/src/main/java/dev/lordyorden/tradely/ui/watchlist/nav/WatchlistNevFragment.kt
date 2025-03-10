package dev.lordyorden.tradely.ui.watchlist.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.adapter.WatchlistNavigationAdapter
import dev.lordyorden.tradely.databinding.FragmentWatchlistNevBinding


class WatchlistNevFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistNevBinding
    private lateinit var demoCollectionPagerAdapter: WatchlistNavigationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchlistNevBinding.inflate(layoutInflater, container, false) //TODO make all fragment do this
        initViews()

        return binding.root
    }

    private fun initViews() {
        demoCollectionPagerAdapter = WatchlistNavigationAdapter(this)
        binding.watchlistVPViews.adapter = demoCollectionPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.watchlistVPViews) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Watchlist"
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_watchlist_alt, null)
                }
                1 -> {
                    tab.text = "Owned Stocks"
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_my_stock, null)
                }
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    binding.watchlistVPViews.setCurrentItem(it, true)
                    //Toast.makeText(context, "Tab selected $it", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    //Toast.makeText(context, "Tab reselected $it", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //pass
            }
        })

    }
}