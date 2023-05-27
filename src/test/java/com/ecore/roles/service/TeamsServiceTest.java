package com.ecore.roles.service;

import static com.ecore.roles.constants.ValidationConstants.TEAM_NOT_FOUND;
import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM_UUID;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.impl.TeamsServiceImpl;

@ExtendWith(MockitoExtension.class)
class TeamsServiceTest {

    @InjectMocks
    private TeamsServiceImpl teamsService;
    @Mock
    private TeamsClient teamsClient;

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team ordinaryCoralLynxTeam = ORDINARY_CORAL_LYNX_TEAM();
        when(teamsClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ordinaryCoralLynxTeam));
        assertNotNull(teamsService.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID));
    }
    
    
    @Test
    void shouldFailWhenTeamIdIsInvalid() {
        User gianniUser = GIANNI_USER();
        when(teamsClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(null));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
        	teamsService.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID);
    	});
        
        assertEquals(exception.getMessage(), format(TEAM_NOT_FOUND, ORDINARY_CORAL_LYNX_TEAM_UUID));
    }
    
    @Test
    void shouldGetTeams() {
        List<Team> expectedResult = List.of(ORDINARY_CORAL_LYNX_TEAM());
		when(teamsClient.getTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(expectedResult));

        List<Team> actualResult = teamsService.getTeams();
        
        assertEquals(actualResult, expectedResult);
    }
}
