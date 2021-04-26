package com.example.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.solver.ArrayLinkedVariables
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter= NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
      val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
                null,
            Response.Listener {
              val newsJSONArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJSONArray.length()) {
                    val newsJSONObject = newsJSONArray.getJSONObject(i)
                    val news = News(
                            newsJSONObject.getString("title"),
                            newsJSONObject.getString("author"),
                            newsJSONObject.getString("url"),
                            newsJSONObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }

}