package com.gitlab.faerytea.ghapi.net.api;

import lombok.Data;

@Data
public class Repo {
    String name;
    String description;
    String language;
    User owner;
}
