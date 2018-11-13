package com.gitlab.faerytea.ghapi.api;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonQualifier;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class User {
    String login;
    @Json(name = "avatar_url") String userpic;
}
