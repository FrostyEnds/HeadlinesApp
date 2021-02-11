package com.example.headlinesapp.models

//data class for articles returned by NewsAPI
data class Article(
        val source: Source?,
        val author: String?,
        val title: String?,
        val description: String?,
        val url: String?,
        val urlToImage: String?,
        val publishedAt: String?,
        val content: String?)