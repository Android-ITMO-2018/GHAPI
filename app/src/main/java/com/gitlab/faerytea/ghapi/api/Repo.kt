package com.gitlab.faerytea.ghapi.api

data class Repo (
    val name: String,
    val description: String? = null,
    val language: String? = null,
    val owner: User
)
