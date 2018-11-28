package com.gitlab.faerytea.ghapi

import android.os.*
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.gitlab.faerytea.ghapi.api.GitHubApi
import com.gitlab.faerytea.ghapi.api.User
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
    private var rxCall: Disposable? = null


    private fun createApi(): GitHubApi {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
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

    fun processResponse(response: Response<List<User>>): List<User> =
        if (!response.isSuccessful)
            throw IllegalStateException(
                "Code: ${response.code()}, errorBody: ${response.errorBody()?.string()}"
            )
        else
            response.body() ?: emptyList()

    fun refresh() {
        val okHttpContributors = api.getContributors("okhttp")

        val okioContributors = api.getContributors("okio")

        val observable = Observables.zip(okHttpContributors, okioContributors) { l1, l2 -> l1 + l2 }

        rxCall = observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            { users ->
                refreshLayout.isRefreshing = false
                this@MainActivity.adapter.setData(users)
            },
            { error ->
                refreshLayout.isRefreshing = false
                adapter.setData(emptyList())
                Toast.makeText(
                    this@MainActivity,
                    error.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        rxCall?.dispose()
        if (userCall != null) userCall!!.cancel()         // …тут мы сносим все контексты из того,
        Picasso.get().cancelTag(MainActivity::class.java) // что может пережить MainActivity
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
}
