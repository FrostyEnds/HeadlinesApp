package com.example.headlinesapp

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.headlinesapp.extensions.hideKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    var results = ArrayList<Article>()
    lateinit var rvSearchResults : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvSearchResults = findViewById(R.id.search_results)
        rvSearchResults.adapter = ArticlesAdaptor(results)
        rvSearchResults.layoutManager = LinearLayoutManager(this)

        val searchText = findViewById<EditText>(R.id.search_text)
        searchText.setOnEditorActionListener { v, actionId, _ ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard()
                    doSearch(v.text.toString())
                    true
                }
                else -> false
            }
        }
    }

    private fun doSearch(searchText : String) {
        val call = NewsAPIClient.service.getEverything(searchText, "en", null, 100)

        call.enqueue(object: Callback<ArticlesResponse> {
            override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("SearchActivity", "Call Success ${response.body()?.totalResults}")
                    results.clear()
                    response.body()?.articles?.let { results.addAll(it) }
                    rvSearchResults.adapter?.let { it.notifyDataSetChanged() }
                    rvSearchResults.smoothScrollToPosition(0)
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("MainActivity", "Call Failed")
            }
        })
    }

}