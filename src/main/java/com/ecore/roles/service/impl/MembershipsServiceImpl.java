package com.ecore.roles.service.impl;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    
    @Override
    public Membership assignRoleToMembership(@NonNull Membership membership) {
        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
        
        Membership savedMembership = membershipRepository.findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId())
        												 .orElseThrow(() -> new ResourceNotFoundException(Membership.class, membership.getTeamId()));        
        savedMembership.setRole(role);
        
        return membershipRepository.save(savedMembership);
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID roleID) {
        return membershipRepository.findByRoleId(roleID);
    }
}
