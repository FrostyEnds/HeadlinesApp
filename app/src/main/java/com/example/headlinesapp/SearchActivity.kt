package com.example.headlinesapp

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.headlinesapp.extensions.hideKeyboard
import com.example.headlinesapp.models.Article
import com.example.headlinesapp.models.ArticlesResponse
import com.example.headlinesapp.services.NewsAPIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    //Track which page of results, from NewsAPI, we are on
    var currentResultsPage = 1
    //Track which search term we are currently getting pages of results for
    var currentSearchTerm = ""
    //List of results with the currentSearchTerm from NewsAPI
    var results = ArrayList<Article>()
    //RecyclerView for articles
    lateinit var rvSearchResults : RecyclerView
    //OnScrollListener added to rvSearchResults
    lateinit var onScrollListener: OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //init rvSearchResults and onScrollListener
        initRecyclerView()

        //Set action to perform when user presses search on keyboard
        var etSearchText = findViewById<EditText>(R.id.search_text)
        etSearchText.setOnEditorActionListener { v, actionId, _ ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard()
                    currentSearchTerm = v.text.toString()
                    getInitialSearchResults()
                    true
                }
                else -> false
            }
        }
    }

    //Initializer for rvSearchResults and onScrollListener
    private fun initRecyclerView() {
        rvSearchResults = findViewById(R.id.search_results)
        rvSearchResults.adapter = ArticlesAdaptor(results)
        val layoutManager = LinearLayoutManager(this)
        rvSearchResults.layoutManager = layoutManager
        onScrollListener = OnScrollListener(layoutManager) { getMoreSearchResults() }
        rvSearchResults.addOnScrollListener(onScrollListener)
    }

    /**
     * getInitialSearchResults()
     * Initial search, for new searches.
     * Resets results, currentResultsPage, and onScrollListener
     * Adds articles from response to results and notifies rvSearchResults the data changed
     * Smooth scrolls to top of rvSearchResults.
     */
    private fun getInitialSearchResults() {
        Log.d("SearchActivity", "Called getInitialSearchResults")
        val call = NewsAPIClient.getNewsAPIService().getEverything(currentSearchTerm, MainActivity.DEFAULT_LANGUAGE, null, MainActivity.PAGE_SIZE)

        call.enqueue(object: Callback<ArticlesResponse> {
            override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("SearchActivity", "Everything Call Success")
                    //reset results, page, and scroll listener
                    results.clear()
                    currentResultsPage = 1
                    onScrollListener.reset()

                    //Add articles to results
                    response.body()?.articles?.let { responseArticles ->
                        results.addAll(responseArticles)
                    }

                    //notify rvSearchResults and smooth scroll to top
                    rvSearchResults.adapter?.let { adaptor -> adaptor.notifyDataSetChanged() }
                    rvSearchResults.smoothScrollToPosition(0)
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("SearchActivity", "Call Failed")
            }
        })
    }

    /**
     * getMoreSearchResults()
     * For subsequent pages for searches for the same currentSearchTerm.
     * Adds articles from response to results and notifies rvSearchResults the data changed
     */
    private fun getMoreSearchResults() {
        Log.d("SearchActivity", "Called getMoreSearchResults on page ${currentResultsPage}")
        currentResultsPage += 1
        val call = NewsAPIClient.getNewsAPIService().getEverything(currentSearchTerm, MainActivity.DEFAULT_LANGUAGE, currentResultsPage, MainActivity.PAGE_SIZE)

        call.enqueue(object: Callback<ArticlesResponse> {
            override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("SearchActivity", "Everything Call Success")
                    val curSize = results.size
                    response.body()?.articles?.let { responseArticles ->
                        if(responseArticles.isNotEmpty()) {
                            //Add articles to results
                            results.addAll(responseArticles)

                            //notify rvSearchResults data changed
                            rvSearchResults.adapter?.let { adaptor -> adaptor.notifyItemInserted(curSize) }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("SearchActivity", "Call Failed")
            }
        })
    }


}