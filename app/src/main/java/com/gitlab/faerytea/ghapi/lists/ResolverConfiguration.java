package com.gitlab.faerytea.ghapi.lists;

import androidx.annotation.IntDef;

import static com.gitlab.faerytea.ghapi.lists.ResolverConfiguration.*;

@IntDef({CONTRIBUTORS, REPOS})
public @interface ResolverConfiguration {
    int CONTRIBUTORS = 0;
    int REPOS = 1;
}
