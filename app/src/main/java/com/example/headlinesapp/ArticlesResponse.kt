package com.example.headlinesapp

data class ArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>) {

}