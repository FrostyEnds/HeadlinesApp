package com.example.headlinesapp

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object NewsAPIClient {

    private const val BASE_URL = "https://newsapi.org"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val service = retrofit.create(NewsAPI::class.java)

}