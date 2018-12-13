package com.gitlab.faerytea.ghapi.lists;

import android.util.Log;

import com.gitlab.faerytea.ghapi.lists.repos.DaggerRepoListComponent;
import com.gitlab.faerytea.ghapi.lists.users.DaggerUserListComponent;
import com.gitlab.faerytea.ghapi.net.DaggerNetworkComponent;
import com.gitlab.faerytea.ghapi.net.NetworkComponent;

import androidx.annotation.NonNull;

class ComponentResolver {
    @NonNull
    static ListActivityComponent resolve(@ResolverConfiguration int config) {
        Log.d("Resolver", "resolve() called with: config = [" + config + "]");
        final NetworkComponent network = DaggerNetworkComponent.create();
        switch (config) {
            case 0: return DaggerUserListComponent.builder().networkComponent(network).build();
            case 1: return DaggerRepoListComponent.builder().networkComponent(network).build();
        }
        throw new IllegalArgumentException(config + " is not a valid configuration!");
    }
}
