package com.example.colearn.repository

import com.example.colearn.models.SearchResponse
import com.example.colearn.models.UnsplashPhoto
import com.example.colearn.network.Result
import com.example.colearn.remoteSource.PhotosRemoteSoruce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Gagan on 27/02/21.
 */
class PhotosRepository @Inject constructor(private val remoteSoruce: PhotosRemoteSoruce) {

    suspend fun getPhotosCollectionById(collectionID:String,pageNumber:Int?) : Result<List<UnsplashPhoto>> {
        val result :Result<List<UnsplashPhoto>>
        withContext(Dispatchers.IO){
            result =remoteSoruce.getPhotosCollectionById(collectionID,pageNumber)
        }
        return result
    }

    suspend fun performSearch(query: String, pageNumber: Int?): Result<SearchResponse> {
        val result : Result<SearchResponse>
        withContext(Dispatchers.IO){
            result = remoteSoruce.searchPhotos(query,pageNumber)
        }
        return result
    }

    suspend fun applyFilters(
        query: String,
        sortBy: String,
        color: String?,
        orientation: String?,
    ):  Result<SearchResponse> {
        val  result: Result<SearchResponse>
        withContext(Dispatchers.IO){
            result = remoteSoruce.applyFilters(query,sortBy,color,orientation)
        }
        return result
    }
}