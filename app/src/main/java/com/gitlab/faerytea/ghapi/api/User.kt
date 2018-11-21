package com.gitlab.faerytea.ghapi.api

import com.squareup.moshi.Json

data class User(
    val login: String,
    @field:Json(name = "avatar_url") val userpic: String? = null
)
