package com.gitlab.faerytea.ghapi.lists.repos;

import com.gitlab.faerytea.ghapi.BuildConfig;
import com.gitlab.faerytea.ghapi.lists.ListActivity_;
import com.gitlab.faerytea.ghapi.lists.ListPresenter;
import com.gitlab.faerytea.ghapi.lists.SimpleAdapter;
import com.gitlab.faerytea.ghapi.net.api.GitHubApi;
import com.gitlab.faerytea.ghapi.net.api.Repo;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import retrofit2.Call;

import static com.gitlab.faerytea.ghapi.lists.ResolverConfiguration.CONTRIBUTORS;

@Accessors(fluent = true)
class ReposPresenter extends ListPresenter<Repo> {
    @Getter
    private final Adapter adapter;
    private final GitHubApi api;
    private Call<List<Repo>> lastCall;
    private String lastQuery = "JakeWharton";

    @Inject
    ReposPresenter(Adapter adapter, GitHubApi api) {
        this.adapter = adapter;
        this.api = api;
        adapter.setListener((ctx, repo) -> ListActivity_.intent(ctx).component(CONTRIBUTORS).initialQuery(repo.getName()).start());
    }

    @Override
    public void refresh() {
        val q = lastQuery;
        lastQuery = "";
        query(q);
    }

    @Override
    public void query(@NonNull String query) {
        if (lastQuery.equals(query)) return;
        if (lastCall != null) lastCall.cancel();
        lastQuery = query;
        lastCall = api.getRepos(query, BuildConfig.API_KEY);
        lastCall.enqueue(new SimpleCall());
        withActivity(a -> a.setTitle(query));
    }
}
