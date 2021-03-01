package com.example.colearn.viewmodels

import androidx.lifecycle.*
import com.example.colearn.BuildConfig
import com.example.colearn.Utils.Constants
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
 * Created by Gagan on 27/02/21.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photosRepository: PhotosRepository
) : ViewModel() {


     var sortBy: String? = "latest"
     var color: String? = null
     var orientation: String? = null

    // ImageDataLiveData

    private var imageViewLiveData = MutableLiveData<ImageGridViewData>()
    val _imageViewLiveData: LiveData<ImageGridViewData>
        get() = imageViewLiveData

    // SearchQueryLiveData
    private var searchQueryLiveData = MutableLiveData<String>()
    val _searchQueryLiveData: LiveData<String>
        get() = searchQueryLiveData

    // LoadingState
    private var loadingLiveData = MutableLiveData<Boolean>()
    val _loadingLiveData: LiveData<Boolean>
        get() = loadingLiveData

    //ErrorState

    private var errorLiveData = MutableLiveData<String>()
    val _errorLiveData: LiveData<String>
        get() = errorLiveData

    //FullViewLiveData
    private var fullViewLiveData = MutableLiveData<String>()
    val _fullViewLiveData: LiveData<String>
        get() = fullViewLiveData

    //empty state live data
    private var emptyStateLiveData = MutableLiveData<Boolean>()
    val _emptyStateLiveData: LiveData<Boolean>
        get() = emptyStateLiveData

    // apply filterLiveData
    private var applyFilterLiveData = MutableLiveData<Boolean>()
    val _applyFilterLiveData: LiveData<Boolean>
        get() = applyFilterLiveData

    val thumbnailList = ArrayList<String>()
    val fullViewList = ArrayList<String>()

    fun getImageCollectionsById(pageNumber: Int?) {

        viewModelScope.launch {
            loadingLiveData.value = true
            val result = photosRepository.getPhotosCollectionById(
                BuildConfig.UNSPLASH_CLIENT_ID,
                pageNumber
            )

            when (result) {
                is Result.Success -> {
                    loadingLiveData.value = false
                    maptoImageData(result.data, result.pageInfo)
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
                imageViewLiveData.value = ImageGridViewData(thumbnailList, fullViewList, pageInfo)
            }
        }

    }


    fun setSearchData(query: String) {
        searchQueryLiveData.value = query
    }

    fun setFullViewImageData(url: String, position: Int) {
        fullViewLiveData.value = url
    }

    fun applyFilter(query: String) {
        viewModelScope.launch {
            val result = photosRepository.applyFilters(query,sortBy!!, color, orientation)
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

    fun setFilterData(filterType: String, filterValue: String?) {

        when (filterType) {
            Constants.SORT_BY_FILTER -> {
                sortBy = filterValue
            }
            Constants.COLOR_FILTER -> {
                color = filterValue
            }
            Constants.ORIENTATION_FILTER -> {
                orientation = filterValue
            }
        }

    }

    fun applyFilterClicked(filterValue: Boolean){
        applyFilterLiveData.value = filterValue
    }

    fun clearData(){
        thumbnailList.clear()
        fullViewList.clear()
    }
}