package com.ecore.roles.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.RolesService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    public static final String DEFAULT_ROLE = "Developer";

    private final RoleRepository roleRepository;

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

}
