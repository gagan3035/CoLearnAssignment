package com.example.colearn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.colearn.R
import com.example.colearn.databinding.FragmentFullViewBinding
import com.example.colearn.viewmodels.HomeViewModel
import com.example.imageloader.imageloader.ImageLoader

/**
 * Created by Gagan on 28/02/21.
 */
class FullViewFragment : Fragment(R.layout.fragment_full_view) {

    private var _binding: FragmentFullViewBinding? = null
    private val binding get() = _binding
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullViewBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        handleClicks()
    }

    private fun handleClicks() {
        binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpUI() {

        homeViewModel._fullViewLiveData.observe(viewLifecycleOwner, {
            ImageLoader.with(requireContext()).load(binding!!.imageView, it)

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}