package com.example.colearn.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colearn.R
import com.example.colearn.Utils.EndlessRecyclerViewScrollListener
import com.example.colearn.databinding.FragmentSearchBinding
import com.example.colearn.ui.FullViewFragment
import com.example.colearn.ui.adapters.ImageGridAdapter
import com.example.colearn.viewmodels.HomeViewModel
import com.example.colearn.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Gagan on 27/02/21.
 */
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var query: String = ""
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private var loadFirstTime = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFirstTime = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        observeData()
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        handleClicks()
        handleInfiniteScroll()
    }

    private fun handleClicks() {
        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.filter.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                val fragment = FilterFragment()
                replace(R.id.fragment_container_view, fragment, null)
                addToBackStack(null)
            }
        }
    }

    private fun handleInfiniteScroll() {

        binding.recyclerView.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(binding.recyclerView.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                searchViewModel.performSearch(query, page)
            }

        })
    }

    private fun observeData() {

        homeViewModel._searchQueryLiveData.observe(viewLifecycleOwner, {
            query = it
            binding.title.text = it
            searchViewModel.clearData()
            searchViewModel.performSearch(it, 1)

        })

        searchViewModel._imageViewLiveData.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as ImageGridAdapter).clearData()
            (binding.recyclerView.adapter as ImageGridAdapter).setData(it.thumbnailUrlList,it.fullViewUrlList)
        })

        searchViewModel._errorLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        searchViewModel._loadingLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
        searchViewModel._emptyStateLiveData.observe(viewLifecycleOwner, {
            if (it) {
                binding.emptyText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })

        homeViewModel._applyFilterLiveData.observe(viewLifecycleOwner, {
            homeViewModel.applyFilter(query)
        })
    }

    private fun setUpViews() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ImageGridAdapter({ url, position -> onItemClicked(url, position) })
        }

    }

    private fun onItemClicked(url: String, position: Int) {
        homeViewModel.setFullViewImageData(url, position)
        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            val fragment = FullViewFragment()
            replace(R.id.fragment_container_view, fragment, null)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadFirstTime = false
        _binding = null
    }
}