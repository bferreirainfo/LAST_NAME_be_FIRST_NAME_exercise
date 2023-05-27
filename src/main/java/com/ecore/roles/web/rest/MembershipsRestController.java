package com.ecore.roles.web.rest;

import static com.ecore.roles.constants.RestConstants.ACTION_SEARCH;
import static com.ecore.roles.constants.RestConstants.APPLICATION_JSON;
import static com.ecore.roles.constants.RestConstants.FIELD_ROLE_ID;
import static com.ecore.roles.constants.RestConstants.V1_ROLES_MEMBERSHIPS;
import static com.ecore.roles.web.dto.MembershipDto.fromModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecore.roles.model.Membership;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.web.MembershipsApi;
import com.ecore.roles.web.dto.MembershipDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = V1_ROLES_MEMBERSHIPS)
public class MembershipsRestController implements MembershipsApi {

    private final MembershipsService membershipsService;

    @Override
    @PostMapping(
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public ResponseEntity<MembershipDto> assignRoleToMembership(
            @NotNull @Valid @RequestBody MembershipDto membershipDto) {
        Membership membership = membershipsService.assignRoleToMembership(membershipDto.toModel());
        return ResponseEntity
                .status(201)
                .body(fromModel(membership));
    }

    @Override
    @GetMapping(
            path = ACTION_SEARCH,
            produces = {APPLICATION_JSON})
    public ResponseEntity<List<MembershipDto>> getMemberships(
            @RequestParam(FIELD_ROLE_ID) UUID roleId) {
        return ResponseEntity
                .status(200)
                .body(membershipsService.getMemberships(roleId).stream()
                        .map(MembershipDto::fromModel)
                        .collect(Collectors.toList()));
    }

}
