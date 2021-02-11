package com.example.headlinesapp.models

//data class for response from everything and top headlines endpoints for NewsAPI
data class ArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>)