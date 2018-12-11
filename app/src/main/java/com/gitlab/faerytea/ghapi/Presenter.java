package com.gitlab.faerytea.ghapi;

import android.util.Log;
import android.widget.Toast;

import com.gitlab.faerytea.ghapi.api.GitHubApi;
import com.gitlab.faerytea.ghapi.api.User;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Accessors(fluent = true)
class Presenter {
    private final GitHubApi api;
    private final Queue<WithActivity> actions = new ArrayDeque<>();
    private MainActivity activity;
    private Call<List<User>> userCall;
    private String lastRepo = "okhttp";
    private static final String LOG_TAG = Presenter.class.getSimpleName();

    Presenter() {
        Log.d(LOG_TAG, "Presenter() called");
        api = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(new OkHttpClient())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(GitHubApi.class);
    }

    void attach(MainActivity a) {
        Log.d(LOG_TAG, "attach() called with: a = [" + a + "]");
        activity = a;
        while (!actions.isEmpty()) actions.remove().perform(a);
    }

    void detach() {
        Log.d(LOG_TAG, "detach() called");
        activity = null;
    }

    void usersByRepo(@NonNull String username) {
        if (userCall != null) userCall.cancel();
        userCall = api.getContributors(username, BuildConfig.API_KEY);
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
                withActivity(a -> {
                    a.refresh.setRefreshing(false);
                    a.adapter.setData(users);
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                withActivity(a -> {
                    a.refresh.setRefreshing(false);
                    a.adapter.setData(Collections.emptyList());
                    Toast.makeText(
                            a,
                            t.getLocalizedMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                });
            }
        });
    }

    void refresh() {
        usersByRepo(lastRepo);
    }

    private void withActivity(WithActivity call) {
        if (activity != null) call.perform(activity);
        else actions.add(call);
    }

    interface WithActivity {
        void perform(@NonNull MainActivity activity);
    }
}
