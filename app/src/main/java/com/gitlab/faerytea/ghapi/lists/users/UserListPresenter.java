package com.gitlab.faerytea.ghapi.lists.users;

import android.util.Log;

import com.gitlab.faerytea.ghapi.ActivityScope;
import com.gitlab.faerytea.ghapi.BuildConfig;
import com.gitlab.faerytea.ghapi.lists.ListActivity_;
import com.gitlab.faerytea.ghapi.lists.ListPresenter;
import com.gitlab.faerytea.ghapi.net.api.GitHubApi;
import com.gitlab.faerytea.ghapi.net.api.User;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import retrofit2.Call;

import static com.gitlab.faerytea.ghapi.lists.ResolverConfiguration.CONTRIBUTORS;
import static com.gitlab.faerytea.ghapi.lists.ResolverConfiguration.REPOS;

@Accessors(fluent = true)
@ActivityScope
public class UserListPresenter extends ListPresenter<User> {
    private final GitHubApi api;
    @Getter
    private final Adapter adapter;
    private Call<List<User>> userCall;
    private String lastRepo = "okhttp";
    private static final String LOG_TAG = UserListPresenter.class.getSimpleName();

    @Inject
    UserListPresenter(GitHubApi api, Adapter adapter) {
        Log.d(LOG_TAG, "UserListPresenter() called");
        this.api = api;
        this.adapter = adapter;
        adapter.setListener((ctx, user) -> ListActivity_.intent(ctx).component(REPOS).initialQuery(user.getLogin()).start());
    }

    public void query(@NonNull String username) {
        if (lastRepo.equals(username)) return;
        lastRepo = username;
        if (userCall != null) userCall.cancel();
        userCall = api.getContributors(username, BuildConfig.API_KEY);
        userCall.enqueue(new SimpleCall());
        withActivity(a -> a.setTitle(username));
    }

    public void refresh() {
        val q = lastRepo;
        lastRepo = "";
        query(q);
    }
}
