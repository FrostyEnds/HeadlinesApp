package com.example.headlinesapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsAPI {

    @Headers("X-Api-Key: 322e82d4f36746c1974790e0d262dba5")
    @GET("/v2/everything")
    fun getEverything(
        @Query("q") query: String?,
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?
    ): Call<ArticlesResponse>

    @Headers("X-Api-Key: 322e82d4f36746c1974790e0d262dba5")
    @GET("/v2/top-headlines")
    fun getTopHeadlines(
        @Query("language") language: String?,
        @Query("country") country: String?,
        @Query("q") query: String?,
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?
    ): Call<ArticlesResponse>
}