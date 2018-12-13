package com.gitlab.faerytea.ghapi.net.api;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Module
public interface GitHubApi {
    @GET("repos/square/{name}/contributors")
    Call<List<User>> getContributors(@Path("name") String repoName, @Query("apiKey") String apiKey);

    @GET("users/{name}/repos")
    Call<List<Repo>> getRepos(@Path("name") String userName, @Query("apiKey") String apiKey);

    @Provides
    @Singleton
    static GitHubApi api(Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }
}
