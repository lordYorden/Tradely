package dev.lordyorden.tradely.ui.watchlist

import androidx.fragment.app.activityViewModels
import dev.lordyorden.tradely.ui.watchlist.filter.FilterStocksViewModel
import dev.lordyorden.tradely.ui.watchlist.filter.StocksViewFragment

class WatchlistFragment : StocksViewFragment() {

    private val watchlistViewModel: WatchlistViewModel by activityViewModels()

    override fun getViewMode(): FilterStocksViewModel {
        return watchlistViewModel
    }
}