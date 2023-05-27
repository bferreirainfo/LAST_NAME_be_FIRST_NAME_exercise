package com.ecore.roles.service.impl;

import static com.ecore.roles.constants.ValidationConstants.USER_NOT_FOUND;
import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.UsersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersClient usersClient;

    public User getUser(UUID id) {
        return Optional.ofNullable(usersClient.getUser(id).getBody())
                .orElseThrow(() -> new ResourceNotFoundException(format(USER_NOT_FOUND, id)));
    }

    public List<User> getUsers() {
        return usersClient.getUsers().getBody();
    }
}
