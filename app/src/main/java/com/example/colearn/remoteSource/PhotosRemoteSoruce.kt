package com.example.colearn.remoteSource

import com.example.colearn.BuildConfig
import com.example.colearn.models.SearchResponse
import com.example.colearn.models.UnsplashPhoto
import com.example.colearn.network.BaseApiService
import com.example.colearn.network.ImageService
import com.example.colearn.network.Result
import javax.inject.Inject

/**
 * Created by Gagan on 27/02/21.
 */
class PhotosRemoteSoruce @Inject constructor(private val imageService: ImageService) :
    BaseApiService<Int, UnsplashPhoto>() {

    suspend fun getPhotosCollectionById(
        collectionId: String,
        pageNumber: Int?
    ): Result<List<UnsplashPhoto>> {

        return apiCall({ imageService.getCollectionById(collectionId, pageNumber, null) }, false)

    }

    suspend fun searchPhotos(query: String, pageNumber: Int?): Result<SearchResponse> {

        return apiCall({
            imageService.searchPhotos(
                BuildConfig.UNSPLASH_CLIENT_ID,
                query,
                pageNumber,
                null
            )
        }, false)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashPhoto>
    ) {
        //callback.onResult()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashPhoto>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashPhoto>) {
        //callback.onResult(params.key,)
    }

    suspend fun applyFilters(
        query: String,
        sortBy: String,
        color: String?,
        orientation: String?
    ): Result<SearchResponse> {

        return apiCall({
            imageService.filterPhotos(
                BuildConfig.UNSPLASH_CLIENT_ID,
                query,
                null,
                null,
                sortBy,
                color,
                orientation
            )
        }, false)
    }
}