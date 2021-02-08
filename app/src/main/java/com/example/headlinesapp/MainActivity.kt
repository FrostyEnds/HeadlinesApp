package com.example.headlinesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        var titles = ArrayList<String>()
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,titles)
        var listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

//        val client = OkHttpClient.Builder()
//            .addInterceptor(ChuckerInterceptor(this))
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://newsapi.org")
//            .addConverterFactory(MoshiConverterFactory.create())
//            .client(client)
//            .build()
//
//        val service = retrofit.create(NewsAPI::class.java)
//        val call = service.getTopHeadlines("en",null,null,null,100)

        val call = NewsAPIClient.service.getTopHeadlines("en",null,null,null,100)

        call.enqueue(object: Callback<ArticlesResponse>{
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                if(response.isSuccessful){
                    Log.e("MainActivity", "Call Success ${response.body()?.totalResults}")
                    val iterator = response.body()?.articles?.iterator()
                    iterator?.forEach { titles.add(it.title) }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("MainActivity", "Call Failed")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_search -> {
            startActivity(Intent(this, SearchActivity::class.java))
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}