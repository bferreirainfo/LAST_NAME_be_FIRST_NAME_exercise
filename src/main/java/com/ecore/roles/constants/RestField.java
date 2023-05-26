package com.ecore.roles.constants;

import static java.lang.String.format;

public class RestField {

    public final String NAME;
    public final String PATH_NAME;

    public RestField(String name) {
        this.NAME = name;
        this.PATH_NAME = format("/{%s}", name);
    }
}
