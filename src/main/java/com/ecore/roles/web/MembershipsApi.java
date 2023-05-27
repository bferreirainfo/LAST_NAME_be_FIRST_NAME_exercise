package com.ecore.roles.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.ecore.roles.web.dto.MembershipDto;


public interface MembershipsApi {

    ResponseEntity<MembershipDto> assignRoleToMembership(
            MembershipDto membership);

    ResponseEntity<List<MembershipDto>> getMemberships(
            UUID roleId);

}
