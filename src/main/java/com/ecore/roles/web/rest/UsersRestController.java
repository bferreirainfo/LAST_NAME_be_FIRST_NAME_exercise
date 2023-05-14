package com.ecore.roles.web.rest;

import static com.ecore.roles.constants.RestConstants.APPLICATION_JSON;
import static com.ecore.roles.constants.RestConstants.USER_ID;
import static com.ecore.roles.constants.RestConstants.V1_USERS;
import static com.ecore.roles.web.dto.UserDto.fromModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecore.roles.service.UsersService;
import com.ecore.roles.web.UsersApi;
import com.ecore.roles.web.dto.UserDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = V1_USERS)
public class UsersRestController implements UsersApi {
	
	
	private final UsersService usersService;

    @Override
    @GetMapping(
            produces = {APPLICATION_JSON})
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity
                .status(200)
                .body(usersService.getUsers().stream()
                        .map(UserDto::fromModel)
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping(
            path = USER_ID,
            produces = {APPLICATION_JSON})
    public ResponseEntity<UserDto> getUser(
            @PathVariable UUID userId) {
        return ResponseEntity
                .status(200)
                .body(fromModel(usersService.getUser(userId)));
    }
}
