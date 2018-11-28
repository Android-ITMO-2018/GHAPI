package com.gitlab.faerytea.ghapi.api

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("repos/square/{name}/contributors")
    fun getContributors(@Path("name") repoName: String): Observable<List<User>>

    @GET("users/{name}/repos")
    fun getRepos(@Path("name") userName: String): Observable<List<Repo>>
}
