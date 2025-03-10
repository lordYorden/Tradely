package dev.lordyorden.tradely.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.databinding.FragmentHomeBinding
import dev.lordyorden.tradely.ui.home.profile_card.ProfileCardFragment
import dev.lordyorden.tradely.ui.watchlist.nav.WatchlistNevFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        val profileFragment = ProfileCardFragment()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.home_FRAME_profile_card, profileFragment)
            .commit()


        val watchlistNevFragment = WatchlistNevFragment()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.home_FRAME_watchlist, watchlistNevFragment)
            .commit()
    }
}