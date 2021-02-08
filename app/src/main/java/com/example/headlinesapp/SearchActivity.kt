package com.example.headlinesapp

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.headlinesapp.extensions.hideKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    var results = ArrayList<String>()
    lateinit var listAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        listAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results)
        var listView = findViewById<ListView>(R.id.search_results)
        listView.adapter = listAdapter

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
        //searchText.requestFocus()
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
                    val iterator = response.body()?.articles?.iterator()
                    iterator?.forEach { results.add(it.title) }
                    listAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("MainActivity", "Call Failed")
            }
        })
    }

}