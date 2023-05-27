package com.ecore.roles.service;

import static com.ecore.roles.constants.ValidationConstants.ROLE_NOT_FOUND;
import static com.ecore.roles.constants.ValidationConstants.ROLE_NOT_FOUND_FOR_USER_AND_TEAM;
import static com.ecore.roles.utils.TestData.DEFAULT_MEMBERSHIP;
import static com.ecore.roles.utils.TestData.DEVELOPER_ROLE;
import static com.ecore.roles.utils.TestData.DEVOPS_ROLE;
import static com.ecore.roles.utils.TestData.GIANNI_USER_UUID;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM_UUID;
import static com.ecore.roles.utils.TestData.TESTER_ROLE;
import static com.ecore.roles.utils.TestData.UUID_1;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.RolesServiceImpl;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {

    @InjectMocks
    private RolesServiceImpl rolesService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipsService membershipsService;

    @Test
    public void shouldCreateRole() {
        Role developerRole = DEVELOPER_ROLE();
        when(roleRepository.save(developerRole)).thenReturn(developerRole);

        Role role = rolesService.createRole(developerRole);

        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> rolesService.createRole(null));
    }

    @Test
    public void shouldReturnRoleWhenRoleIdExists() {
        Role developerRole = DEVELOPER_ROLE();
        when(roleRepository.findById(developerRole.getId())).thenReturn(Optional.of(developerRole));

        Role role = rolesService.getRole(developerRole.getId());

        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRole(UUID_1));
        assertEquals(format(ROLE_NOT_FOUND, UUID_1), exception.getMessage());
    }

    @Test
    void shoulGetdRoles() {
        List<Role> expectedResult = List.of(DEVOPS_ROLE(), TESTER_ROLE(), DEVELOPER_ROLE());
        when(roleRepository.findAll()).thenReturn(expectedResult);
        List<Role> actualResult = rolesService.getRoles();

        assertEquals(actualResult, expectedResult);
    }

    @Test
    void shouldGetRoleByUserIdAndTeamId() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();

        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.of(expectedMembership));

        Role actualResult = rolesService.getRoleByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId());

        rolesService.getRoleByUserIdAndTeamId(GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID);

        assertEquals(actualResult, expectedMembership.getRole());
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenUserValuesAreNull() {
        assertThrows(NullPointerException.class,
                () -> rolesService.getRoleByUserIdAndTeamId(null, ORDINARY_CORAL_LYNX_TEAM_UUID));

        assertThrows(NullPointerException.class,
                () -> rolesService.getRoleByUserIdAndTeamId(GIANNI_USER_UUID, null));
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenUserIsInvalid() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();

        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoleByUserIdAndTeamId(GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID));

        assertEquals(
                format(ROLE_NOT_FOUND_FOR_USER_AND_TEAM, GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID),
                exception.getMessage());
    }
}
