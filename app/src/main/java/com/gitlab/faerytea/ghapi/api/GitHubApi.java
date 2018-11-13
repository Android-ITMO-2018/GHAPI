package com.gitlab.faerytea.ghapi.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApi {
    @GET("repos/square/{name}/contributors")
    Call<List<User>> getContributors(@Path("name") String repoName, @Query("apiKey") String apiKey);

    @GET("users/{name}/repos")
    Call<List<Repo>> getRepos(@Path("name") String userName);
}
