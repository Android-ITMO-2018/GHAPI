package com.gitlab.faerytea.ghapi

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast

import com.gitlab.faerytea.ghapi.api.GitHubApi
import com.gitlab.faerytea.ghapi.api.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.picasso.Picasso

import java.lang.reflect.ParameterizedType
import java.util.Collections

import lombok.SneakyThrows
import lombok.`val`
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var client: OkHttpClient
    private lateinit var moshi: Moshi
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var api: GitHubApi
    private var userCall: Call<List<User>>? = null

    private fun createApi(): GitHubApi {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GitHubApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        client = OkHttpClient.Builder().build()
        moshi = Moshi.Builder().build()
        adapter = Adapter()
        api = createApi()
        recycler = findViewById(R.id.recycler)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        refreshLayout = findViewById(R.id.refresh)
        refreshLayout.setOnRefreshListener { this.refresh() }
    }

    fun refresh() {
        if (userCall != null) userCall!!.cancel()
        userCall = api.getContributors("okhttp", BuildConfig.API_KEY)
        userCall!!.enqueue(object : Callback<List<User>> { // тут может протечь контекст, но…
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (!response.isSuccessful || response.body() == null) {
                    val errorBody = response.errorBody()
                    val str = errorBody?.string()
                    onFailure(call, Throwable(str))
                    return
                }
                val users = response.body()
                if (users == null) onFailure(call, IllegalArgumentException("Users were null"))
                handler.post {
                    refreshLayout.isRefreshing = false
                    Log.d(LOG_TAG, "onResponse: " + users!!.size)
                    this@MainActivity.adapter.setData(users)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                handler.post {
                    refreshLayout.isRefreshing = false
                    adapter.setData(emptyList())
                    Toast.makeText(
                        this@MainActivity,
                        t.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (userCall != null) userCall!!.cancel()         // …тут мы сносим все контексты из того,
        Picasso.get().cancelTag(MainActivity::class.java) // что может пережить MainActivity
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
}
