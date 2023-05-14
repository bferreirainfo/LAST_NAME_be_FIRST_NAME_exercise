package com.ecore.roles.exception;

import static java.lang.String.format;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public <T> ResourceNotFoundException(Class<T> resource, UUID id) {
        super(format("%s %s not found", resource.getSimpleName(), id));
    }

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
