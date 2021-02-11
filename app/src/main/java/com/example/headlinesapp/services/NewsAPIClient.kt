package com.example.headlinesapp.services

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * NewsAPI client
 * Provides an initializer with a chucker interceptor (to see live http traffic through notifications)
 * and an initializer without the interceptor
 * Use getNewsAPIService to get the service to call NewsAPI endpoints
 */
object NewsAPIClient {

    private const val BASE_URL = "https://newsapi.org"

    private lateinit var service : NewsAPI

    /**
     * Getter for service, will call init() without context if it hasn't been initialized
     */
    fun getNewsAPIService() : NewsAPI {
        if (!this::service.isInitialized) {
            init()
        }
        return service
    }

    /**
     * Initializer with context in case you want to have the chucker interceptor
     * @param context an android context for the chucker interceptor
     */
    fun init(context: Context) {
        Log.d("NewsAPIClient", "NewsAPIClient initialized WITH context.")

        //Chucker interceptor for showing HTTP traffic
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .alwaysReadResponseBody(false)
                .build()

        //Client with the interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .build()

        //Build retrofit for NewsAPI with client above
        // and Moshi converter to deserialize and marshal response
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        //Create services in NewsAPI interface
        service = retrofit.create(NewsAPI::class.java)
    }

    /**
     * Initialize without parameters if chucker interceptor isn't needed
     */
    fun init() {
        Log.d("NewsAPIClient", "NewsAPIClient initialized without context.")

        //Build retrofit for NewsAPI with Moshi converter to deserialize and marshal response
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        //Create services in NewsAPI interface
        service = retrofit.create(NewsAPI::class.java)
    }

}