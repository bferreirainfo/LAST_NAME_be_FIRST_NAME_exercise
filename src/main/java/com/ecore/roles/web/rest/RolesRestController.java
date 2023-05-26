package com.ecore.roles.web.rest;

import static com.ecore.roles.constants.RestConstants.APPLICATION_JSON;
import static com.ecore.roles.constants.RestConstants.FIELD_ROLE_ID;
import static com.ecore.roles.constants.RestConstants.FIELD_TEAM_ID;
import static com.ecore.roles.constants.RestConstants.FIELD_USER_ID;
import static com.ecore.roles.constants.RestConstants.V1_ROLES;
import static com.ecore.roles.web.dto.RoleDto.fromModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecore.roles.constants.RestConstants;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = V1_ROLES)
public class RolesRestController implements RolesApi {

    private final RolesService rolesService;

    @Override
    @PostMapping(
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public ResponseEntity<RoleDto> createRole(
            @Valid @RequestBody RoleDto role) {
        return ResponseEntity
                .status(201)
                .body(fromModel(rolesService.createRole(role.toModel())));
    }

    @Override
    @GetMapping(
            produces = {APPLICATION_JSON})
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity
                .status(200)
                .body(rolesService.getRoles().stream()
                        .map(RoleDto::fromModel)
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping(
            path = RestConstants.PATH_ROLE_ID,
            produces = {APPLICATION_JSON})
    public ResponseEntity<RoleDto> getRole(
            @PathVariable(FIELD_ROLE_ID) UUID roleId) {
        return ResponseEntity
                .status(200)
                .body(fromModel(rolesService.getRole(roleId)));
    }

    @Override
    @GetMapping(
            path = RestConstants.ACTION_SEARCH,
            produces = {APPLICATION_JSON})
    public ResponseEntity<RoleDto> searchRole(
            @RequestParam(FIELD_USER_ID) UUID userId,
            @RequestParam(FIELD_TEAM_ID) UUID teamId) {
        return ResponseEntity
                .status(200)
                .body(fromModel(rolesService.getRoleByUserIdAndTeamId(userId, teamId)));
    }

}
