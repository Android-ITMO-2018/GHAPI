package com.gitlab.faerytea.ghapi.net;

import com.gitlab.faerytea.ghapi.net.api.GitHubApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GitHubApi.class, RetrofitModule.class})
public interface NetworkComponent {
    GitHubApi api();
}
