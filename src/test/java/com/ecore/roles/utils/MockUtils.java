package com.ecore.roles.utils;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockUtils {

    public static void mockGetUserById(MockRestServiceServer mockServer, UUID userId, User user) {
        try {
            mockServer.expect(requestTo("http://test.com/users/" + userId))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(user)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void mockGetTeamById(MockRestServiceServer mockServer, UUID teamId, Team team) {
        try {
            mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://test.com/teams/" + teamId))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(team)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public static void mockGetTeams(MockRestServiceServer mockServer, Team team) {
        try {
            List<Team> result = team != null ? List.of(team) : List.of();
			mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://test.com/teams"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(result)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
        
    public static void mockGetTeam(MockRestServiceServer mockServer, UUID teamID, Object result) {
        try {
            mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://test.com/teams/"+ teamID))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(result)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
