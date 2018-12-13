package com.gitlab.faerytea.ghapi.lists.users;

import com.gitlab.faerytea.ghapi.ActivityScope;
import com.gitlab.faerytea.ghapi.lists.ListActivityComponent;
import com.gitlab.faerytea.ghapi.net.NetworkComponent;

import dagger.Component;

@ActivityScope
@Component(dependencies = NetworkComponent.class, modules = UserListModule.class)
interface UserListComponent extends ListActivityComponent {
}
