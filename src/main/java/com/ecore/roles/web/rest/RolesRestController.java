package com.ecore.roles.web.rest;

import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.constants.RestConstants.APPLICATION_JSON;
import static com.ecore.roles.constants.RestConstants.V1_ROLES;
import static com.ecore.roles.web.dto.RoleDto.fromModel;

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
            path = "/{roleId}",
            produces = {APPLICATION_JSON})
    public ResponseEntity<RoleDto> getRole(
            @PathVariable UUID roleId) {
        return ResponseEntity
                .status(200)
                .body(fromModel(rolesService.getRole(roleId)));
    }

    @Override
    @GetMapping(
            path = "/search",
            produces = {APPLICATION_JSON})
    public ResponseEntity<RoleDto> searchRole(
            @RequestParam UUID userID,
            @RequestParam UUID teamID) {
        return ResponseEntity
                .status(200)
                .body(fromModel(rolesService.getRoleByUserIdAndTeamID(userID, teamID)));
    }

}
