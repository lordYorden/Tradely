package dev.lordyorden.tradely.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.lordyorden.tradely.ui.watchlist.DemoObjectFragment
import dev.lordyorden.tradely.ui.watchlist.WatchlistFragment
import dev.lordyorden.tradely.ui.watchlist.my_stock.MyStockFragment

class WatchlistNavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        val fragment = DemoObjectFragment()
        fragment.arguments = Bundle().apply {
            // The object is just an integer.
            putInt(ARG_OBJECT, position + 1)
        }

        return when (position) {
            0 -> WatchlistFragment()
            1 -> MyStockFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        private const val ARG_OBJECT = "object"
    }
}