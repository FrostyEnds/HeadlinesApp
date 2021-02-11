package com.example.headlinesapp.services

import com.example.headlinesapp.models.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//Endpoints for NewsAPI
interface NewsAPI {

    //Everything endpoint for NewsAPI, for searching everything
    //https://newsapi.org/docs/endpoints/everything
    //This key is just stored here for simplicity, it should really be more secure
    @Headers("X-Api-Key: 322e82d4f36746c1974790e0d262dba5")
    @GET("/v2/everything")
    fun getEverything(
            @Query("q") query: String?,
            @Query("language") language: String?,
            @Query("page") page: Int?,
            @Query("pageSize") pageSize: Int?
    ): Call<ArticlesResponse>

    //Top Headlines endpoint for NewsAPI
    //https://newsapi.org/docs/endpoints/top-headlines
    //This key is just stored here for simplicity, it should really be more secure
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