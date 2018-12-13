package com.gitlab.faerytea.ghapi.lists.repos;

import com.gitlab.faerytea.ghapi.lists.ListPresenter;
import com.gitlab.faerytea.ghapi.lists.users.UserListPresenter;

import dagger.Binds;
import dagger.Module;

@Module
interface RepoListModule {
    @Binds
    ListPresenter<?> providePresenter(ReposPresenter presenter);
}
