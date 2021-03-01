package com.example.colearn.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.colearn.R
import com.example.colearn.Utils.EndlessRecyclerViewScrollListener
import com.example.colearn.databinding.FragmentHomeBinding
import com.example.colearn.ui.FullViewFragment
import com.example.colearn.ui.adapters.ImageGridAdapter
import com.example.colearn.ui.search.SearchFragment
import com.example.colearn.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Gagan on 27/02/21.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var totalPages = -1

    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        observeData()
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        handleClicks()
        handleInfiniteScroll()
    }

    private fun handleInfiniteScroll() {

        binding.recyclerView.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(binding.recyclerView.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                homeViewModel.getImageCollectionsById(page)
            }

        })
    }

    private fun handleClicks() {

        binding.search.setOnClickListener {
            if (!TextUtils.isEmpty(binding.searchText.text.toString())) {
                homeViewModel.setSearchData(binding.searchText.text.toString())
                activity?.supportFragmentManager?.commit {
                    setReorderingAllowed(true)
                    val fragment = SearchFragment()
                    replace(R.id.fragment_container_view, fragment, null)
                    addToBackStack(null)
                }
            }
        }

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun observeData() {
        homeViewModel._imageViewLiveData.observe(viewLifecycleOwner, {
            totalPages = it.pageNumber?: 0
            (binding.recyclerView.adapter as ImageGridAdapter).clearData()
            (binding.recyclerView.adapter as ImageGridAdapter).setData(it.thumbnailUrlList,it.fullViewUrlList)
        })

        homeViewModel._errorLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        homeViewModel._loadingLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setUpViews() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ImageGridAdapter { url, position -> onItemClicked(url, position) }

        }
       homeViewModel.clearData()
        homeViewModel.getImageCollectionsById(1)
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
        _binding = null
    }
}