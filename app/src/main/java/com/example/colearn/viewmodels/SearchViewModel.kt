package com.example.colearn.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colearn.models.ImageGridViewData
import com.example.colearn.models.UnsplashPhoto
import com.example.colearn.network.Result
import com.example.colearn.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Gagan on 01/03/21.
 */

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val photosRepository: PhotosRepository
) : ViewModel() {

    val thumbnailList = ArrayList<String>()
    val fullViewList = ArrayList<String>()

    // LoadingState
    private var loadingLiveData = MutableLiveData<Boolean>()
    val _loadingLiveData: LiveData<Boolean>
        get() = loadingLiveData

    // ImageDataLiveData

    private var imageViewLiveData = MutableLiveData<ImageGridViewData>()
    val _imageViewLiveData: LiveData<ImageGridViewData>
        get() = imageViewLiveData

    //
    //ErrorState

    private var errorLiveData = MutableLiveData<String>()
    val _errorLiveData: LiveData<String>
        get() = errorLiveData

    //empty state live data
    private var emptyStateLiveData = MutableLiveData<Boolean>()
    val _emptyStateLiveData: LiveData<Boolean>
        get() = emptyStateLiveData


    fun performSearch(query: String, pageNumber: Int?) {
        viewModelScope.launch {
            loadingLiveData.value = true
            val result = photosRepository.performSearch(query, pageNumber)
            when (result) {
                is Result.Success -> {
                    loadingLiveData.value = false
                    maptoImageData(result.data.results, result.data.total_pages)
                }
                is Result.Error -> {
                    loadingLiveData.value = false
                    errorLiveData.value = result.exception.toString()
                }
                Result.Loading -> {
                    loadingLiveData.value = true
                }
            }
        }
    }

    /*
    * method to convert the Api response to UI format
    * */
    private fun maptoImageData(data: List<UnsplashPhoto>, pageInfo: Int?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                data.forEach {
                    thumbnailList.add(it.urls.thumb!!)
                    fullViewList.add(it.urls.regular!!)
                }
            }
            if (thumbnailList.size == 0) {
                emptyStateLiveData.value = true
            } else {
                emptyStateLiveData.value = false
                Log.d("GAGAN", "maptoImageData: data update")
                imageViewLiveData.value = ImageGridViewData(thumbnailList, fullViewList, pageInfo)
            }
        }

    }

    fun clearData(){
        thumbnailList.clear()
        fullViewList.clear()
    }
}