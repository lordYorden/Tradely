package dev.lordyorden.tradely.ui.watchlist.my_stock

import androidx.fragment.app.activityViewModels
import dev.lordyorden.tradely.ui.watchlist.filter.FilterStocksViewModel
import dev.lordyorden.tradely.ui.watchlist.filter.StocksViewFragment

class MyStockFragment : StocksViewFragment() {

    private val myStockViewModel: MyStockViewModel by activityViewModels()

    override fun getViewMode(): FilterStocksViewModel {
        return myStockViewModel
    }
}