package com.gitlab.faerytea.ghapi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.gitlab.faerytea.ghapi.api.GitHubApi;
import com.gitlab.faerytea.ghapi.api.User;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final ParameterizedType LIST_OF_REPOS_TYPE = Types.newParameterizedType(List.class, User.class);
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private final Handler handler = new Handler(Looper.getMainLooper());
    OkHttpClient client;
    Moshi moshi;
    RecyclerView recycler;
    Adapter adapter;
    Call<List<User>> userCall;
    SwipeRefreshLayout refreshLayout;
    GitHubApi api;

    private GitHubApi createApi() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(GitHubApi.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (client == null) {
            client = new OkHttpClient.Builder().build();
            moshi = new Moshi.Builder().build();
            adapter = new Adapter();
            api = createApi();
        }
        recycler = findViewById(R.id.recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this::refresh);
    }

    public void refresh() {
        if (userCall != null) userCall.cancel();
        userCall = api.getContributors("okhttp", BuildConfig.API_KEY);
        userCall.enqueue(new Callback<List<User>>() { // тут может протечь контекст, но…
            @Override
            @SneakyThrows // ← так вообще почти никогда делать не надо
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    final ResponseBody errorBody = response.errorBody();
                    final String str = errorBody == null ? null : errorBody.string();
                    onFailure(call, new Throwable(str));
                    return;
                }
                val users = response.body();
                handler.post(() -> {
                    refreshLayout.setRefreshing(false);
                    Log.d(LOG_TAG, "onResponse: " + users.size()); // Так делать не надо без проверок на null
                    MainActivity.this.adapter.setData(users);
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                handler.post(() -> {
                    refreshLayout.setRefreshing(false);
                    adapter.setData(Collections.emptyList());
                    Toast.makeText(
                            MainActivity.this,
                            t.getLocalizedMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userCall != null) userCall.cancel();     // …тут мы сносим все контексты из того,
        Picasso.get().cancelTag(MainActivity.class); // что может пережить MainActivity
    }
}
