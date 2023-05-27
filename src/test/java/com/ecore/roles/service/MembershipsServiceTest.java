package com.ecore.roles.service;

import static com.ecore.roles.constants.ValidationConstants.INVALID_ROLE_OBJECT;
import static com.ecore.roles.constants.ValidationConstants.MEMBERSHIP_ALREADY_EXISTS;
import static com.ecore.roles.utils.TestData.DEFAULT_MEMBERSHIP;
import static com.ecore.roles.utils.TestData.DEVELOPER_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.MembershipsServiceImpl;
import com.ecore.roles.utils.TestData;

@ExtendWith(MockitoExtension.class)
class MembershipsServiceTest {

    @InjectMocks
    private MembershipsServiceImpl membershipsService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UsersService usersService;
    @Mock
    private TeamsService teamsService;

    @Test
    public void shouldCreateMembership() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        when(roleRepository.findById(expectedMembership.getRole().getId()))
                .thenReturn(Optional.ofNullable(DEVELOPER_ROLE()));

        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.empty());

        when(teamsService.getTeam(expectedMembership.getTeamId()))
                .thenReturn(TestData.ORDINARY_CORAL_LYNX_TEAM());

        when(membershipRepository
                .save(expectedMembership))
                        .thenReturn(expectedMembership);

        Membership actualMembership = membershipsService.assignRoleToMembership(expectedMembership);

        assertNotNull(actualMembership);
        assertEquals(actualMembership, expectedMembership);
        verify(roleRepository).findById(expectedMembership.getRole().getId());
    }

    @Test
    public void shouldFailToCreateMembershipWhenMembershipsIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.assignRoleToMembership(null));
    }

    @Test
    public void shouldFailToCreateMembershipWhenItExists() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.of(expectedMembership));

        ResourceExistsException exception = assertThrows(ResourceExistsException.class,
                () -> membershipsService.assignRoleToMembership(expectedMembership));

        assertEquals(MEMBERSHIP_ALREADY_EXISTS, exception.getMessage());
        verify(roleRepository, times(0)).getById(any());
        verify(usersService, times(0)).getUser(any());
        verify(teamsService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidRole() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        expectedMembership.setRole(null);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipsService.assignRoleToMembership(expectedMembership));

        assertEquals(INVALID_ROLE_OBJECT, exception.getMessage());
        verify(membershipRepository, times(0)).findByUserIdAndTeamId(any(), any());
        verify(roleRepository, times(0)).getById(any());
        verify(usersService, times(0)).getUser(any());
        verify(teamsService, times(0)).getTeam(any());
        
    }

    @Test
    public void shouldFailToGetMembershipsWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.getMemberships(null));
    }
    
    @Test
    public void shouldGetMemberships() {
    	Membership expectedMembership = DEFAULT_MEMBERSHIP();
    	List<Membership> expectedResult = List.of(expectedMembership);
		when(membershipRepository.findByRoleId(expectedMembership.getRole().getId())).thenReturn(expectedResult);
    	
    	List<Membership> actualResult = membershipsService.getMemberships(expectedMembership.getRole().getId());
    	
    	assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void shouldGetMembershipsButWithEmptyList() {
    	Membership expectedMembership = DEFAULT_MEMBERSHIP();
    	List<Membership> expectedResult = List.of();
		when(membershipRepository.findByRoleId(expectedMembership.getRole().getId())).thenReturn(expectedResult);
    	
    	List<Membership> actualResult = membershipsService.getMemberships(expectedMembership.getRole().getId());
    	
    	assertEquals(expectedResult, actualResult);
    }

}
