package com.gitlab.faerytea.ghapi.api;

import lombok.Data;

@Data
public class Repo {
    String name;
    String description;
    String language;
    User owner;
}
