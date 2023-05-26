package com.ecore.roles.api;

import static com.ecore.roles.utils.MockUtils.mockGetTeam;
import static com.ecore.roles.utils.MockUtils.mockGetTeams;
import static com.ecore.roles.utils.RestAssuredHelper.getTeam;
import static com.ecore.roles.utils.RestAssuredHelper.getTeams;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM_UUID;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.constants.ValidationConstants;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.TeamDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApiTest {
	
	@Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;
	
    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }
    
    @Test
    void shouldGetAllTeams() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();

        mockGetTeams(mockServer, expectedTeam);
        
        TeamDto[] actualTeams = getTeams()
                .statusCode(200)
                .extract().as(TeamDto[].class);

        assertThat(actualTeams.length).isEqualTo(1);
        assertThat(actualTeams[0]).isEqualTo(TeamDto.fromModel(expectedTeam));
    }
    
    @Test
    void shouldGetAllTeamsButReturnsEmptyList() {
        mockGetTeams(mockServer, null);
        
        TeamDto[] actualTeams = getTeams()
                .statusCode(200)
                .extract().as(TeamDto[].class);

        assertThat(actualTeams.length).isEqualTo(0);
    }

    @Test
    void shouldGetTeamById() {
    	Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
		mockGetTeam(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, expectedTeam);
        
        getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID)
	        .statusCode(200)
	        .body("id", equalTo(expectedTeam.getId().toString()))
	        .body("name", equalTo(expectedTeam.getName()));
    }
    
    @Test
    void shouldFailToGetTeamById() {
    	mockGetTeam(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, null);
        getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID)
        		.validate(404, format(ValidationConstants.TEAM_NOT_FOUND, ORDINARY_CORAL_LYNX_TEAM_UUID));
    }
    
}
