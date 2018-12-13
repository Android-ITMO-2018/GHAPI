package com.gitlab.faerytea.ghapi.net.api;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class User {
    String login;
    @Json(name = "avatar_url") String userpic;
}
