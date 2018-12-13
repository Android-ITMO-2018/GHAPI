package com.gitlab.faerytea.ghapi.lists.users;

import com.gitlab.faerytea.ghapi.lists.ListPresenter;

import dagger.Binds;
import dagger.Module;

@Module
interface UserListModule {
    @Binds
    ListPresenter<?> providePresenter(UserListPresenter presenter);
}
