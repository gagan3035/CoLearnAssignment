package com.example.colearn.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colearn.R
import com.example.colearn.Utils.Constants
import com.example.colearn.databinding.FragmentFilterBinding
import com.example.colearn.models.FilterItem
import com.example.colearn.ui.adapters.FilterAdapter
import com.example.colearn.ui.adapters.ImageGridAdapter
import com.example.colearn.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Gagan on 01/03/21.
 */
@AndroidEntryPoint
class FilterFragment : Fragment(R.layout.fragment_filter) {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding

    private val searchViewModel: HomeViewModel by activityViewModels()


    private val filterList1 = listOf<FilterItem>(
        FilterItem("Sort By", Constants.SORT_BY_FILTER),
        FilterItem("Relevance", Constants.OrderByFilter.ORDER_BY_RELEVANT, true),
        FilterItem("Newest", Constants.OrderByFilter.ORDER_BY_LATEST)
    )
    private val filterList2 = listOf<FilterItem>(
        FilterItem("Color", Constants.COLOR_FILTER),
        FilterItem("Any", null, true),
        FilterItem("black_white", Constants.OrderByColor.ORDER_BY_BNW)
    )

    private val filterList3 = listOf<FilterItem>(
        FilterItem("Orientation", Constants.ORIENTATION_FILTER),
        FilterItem("Any", null, true),
        FilterItem("Portrait", Constants.OrderByOrientation.ORDER_BY_PORTRAIT),
        FilterItem("Landscape", Constants.OrderByOrientation.ORDER_BY_LANDSCAPE),
        FilterItem("Square", Constants.OrderByOrientation.ORDER_BY_SQUARISH)
    )

    private lateinit var adapter1: FilterAdapter
    private lateinit var adapter2: FilterAdapter

    private lateinit var adapter3: FilterAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapterData()
        setUpUI()
        handleClicks()
    }

    private fun setUpAdapterData() {
        adapter1 = FilterAdapter(filterList1,
            { filterValue, filterType -> onItemClicked(filterValue, filterType) })
        adapter2 = FilterAdapter(filterList2,
            { filterValue, filterType -> onItemClicked(filterValue, filterType) })
        adapter3 = FilterAdapter(filterList3,
            { filterValue, filterType -> onItemClicked(filterValue, filterType) })
    }

    private fun handleClicks() {

        binding?.back?.setOnClickListener {
            activity?.onBackPressed()
        }

        binding?.applyFilter?.setOnClickListener {
            searchViewModel.applyFilterClicked(true)
            activity?.onBackPressed()
        }

        binding?.clear?.setOnClickListener {
            adapter1.clear()
            adapter2.clear()
            adapter3.clear()
            (binding?.recyclerView?.adapter as ConcatAdapter).notifyDataSetChanged()
        }
    }

    private fun setUpUI() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(adapter1, adapter2, adapter3)
        }
    }

    private fun onItemClicked(filterValue: String?, filterType: String) {
        searchViewModel.setFilterData(filterType, filterValue)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}