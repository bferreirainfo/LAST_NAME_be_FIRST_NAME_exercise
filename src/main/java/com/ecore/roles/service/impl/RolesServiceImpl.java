package com.ecore.roles.service.impl;

import static com.ecore.roles.constants.ValidationConstants.ROLE_NOT_FOUND_FOR_USER_AND_TEAM;
import static java.lang.String.format;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.RolesService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    public static final String DEFAULT_ROLE = "Developer";

    private final RoleRepository roleRepository;
    private final MembershipRepository membershipRepository;

    @Override
    public Role createRole(@NonNull Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResourceExistsException(Role.class);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role getRole(@NonNull UUID roleID) {
        return roleRepository.findById(roleID)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, roleID));
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleByUserIdAndTeamId(@NonNull UUID id, @NonNull UUID teamID) {
        Membership membership = membershipRepository.findByUserIdAndTeamId(id, teamID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        format(ROLE_NOT_FOUND_FOR_USER_AND_TEAM, id, teamID)));
        return membership.getRole();
    }

}
