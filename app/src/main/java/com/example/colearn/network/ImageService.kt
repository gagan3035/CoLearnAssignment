package com.example.colearn.network

import com.example.colearn.models.SearchResponse
import com.example.colearn.models.UnsplashPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Gagan on 27/02/21.
 */
interface ImageService {

    //TODO need to remove hardcoded
    // collection id is hardcoded here
    @GET("/collections/2423569/photos")
    suspend fun getCollectionById(
        @Query("client_id") photoId: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Response<List<UnsplashPhoto>>

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("client_id") clientId: String,
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
    ) : Response<SearchResponse>

    @GET("/search/photos")
    suspend fun filterPhotos(
        @Query("client_id") clientId: String,
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String,
        @Query("color") color: String?,
        @Query("orientation") orientation: String?
    ) : Response<SearchResponse>
}