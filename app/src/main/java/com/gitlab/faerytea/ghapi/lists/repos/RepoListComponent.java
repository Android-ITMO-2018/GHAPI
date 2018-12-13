package com.gitlab.faerytea.ghapi.lists.repos;

import com.gitlab.faerytea.ghapi.ActivityScope;
import com.gitlab.faerytea.ghapi.lists.ListActivityComponent;
import com.gitlab.faerytea.ghapi.net.NetworkComponent;

import dagger.Component;

@ActivityScope
@Component(dependencies = NetworkComponent.class, modules = RepoListModule.class)
interface RepoListComponent extends ListActivityComponent {
}
