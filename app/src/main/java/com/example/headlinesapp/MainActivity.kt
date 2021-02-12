package com.example.headlinesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.headlinesapp.models.Article
import com.example.headlinesapp.models.ArticlesResponse
import com.example.headlinesapp.services.NewsAPIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object{
        //Number of articles per page requested from NewsAPI
        const val PAGE_SIZE = 25
        //Language of articles requested from NewsAPI
        const val DEFAULT_LANGUAGE = "en"
    }

    //Track which page of results, from NewsAPI, we are on
    var currentHeadlinesPage = 1
    //list of top headlines we have gotten from NewsAPI
    var topHeadlinesList = ArrayList<Article>()
    //RecyclerView to display topHeadlinesList
    lateinit var rvTopHeadlines : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        //Init NewsAPIClient with chucker interceptor
        NewsAPIClient.init(this)

        //Init rvTopHeadlines
        initRecyclerView()

        //First call to NewsAPI to populate topHeadlinesList
        getTopHeadlines()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_search -> {
            //Launch search activity
            startActivity(Intent(this, SearchActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    //Initializer for rvTopHeadlines
    private fun initRecyclerView() {
        rvTopHeadlines = findViewById(R.id.rvTopHeadlines)
        rvTopHeadlines.adapter = ArticlesAdaptor(topHeadlinesList)
        val layoutManager = LinearLayoutManager(this)
        rvTopHeadlines.layoutManager = layoutManager
        rvTopHeadlines.addOnScrollListener(OnScrollListener(layoutManager) { getTopHeadlines() })
    }

    /**
     * getTopHeadlines()
     * Get's more top headlines for topHeadlinesList.
     * Adds articles from response to topHeadlinesList and notifies rvTopHeadlines
     */
    private fun getTopHeadlines() {
        Log.d("MainActivity", "Called getTopHeadlines on page ${currentHeadlinesPage}")

        val call = NewsAPIClient.getNewsAPIService().getTopHeadlines(DEFAULT_LANGUAGE,null,null, currentHeadlinesPage, PAGE_SIZE)

        //increment page to request from API next time
        currentHeadlinesPage += 1

        call.enqueue(object: Callback<ArticlesResponse>{
            override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("MainActivity", "Top Headlines Call Success")

                    response.body()?.articles?.let { responseArticles ->
                        if(responseArticles.isNotEmpty()) {
                            var curSize = topHeadlinesList.size

                            //Add articles in response to topHeadlinesList
                            topHeadlinesList.addAll(responseArticles)

                            //Notify recyclerview that data changed
                            rvTopHeadlines.adapter?.let { it.notifyItemInserted(curSize) }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("MainActivity", "Call Failed")
            }
        })
    }

}