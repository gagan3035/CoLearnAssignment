package com.example.colearn.models

data class SearchResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)
