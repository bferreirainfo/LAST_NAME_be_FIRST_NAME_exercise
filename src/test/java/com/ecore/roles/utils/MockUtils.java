package com.ecore.roles.utils;

import static com.ecore.roles.constants.RestConstants.API_TEAMS;
import static com.ecore.roles.constants.RestConstants.API_USERS;
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

    private static final String BASE = "http://test.com/";

    private static String GetUri(String apiName) {
		return BASE + apiName;
	}
    
    private static String GetUriById(String apiName, UUID id) {
		return GetUri(apiName) + "/" + id;
	}
    
	public static void mockGetTeams(MockRestServiceServer mockServer, Team team) {
        mockGetListWithOneElement(mockServer, GetUri(API_TEAMS), team);
    }

    public static void mockGetTeamById(MockRestServiceServer mockServer, UUID id, Object expectedResult) {
    	mockGet(mockServer, GetUriById(API_TEAMS, id), expectedResult);
    }
    
	public static void mockGetUsers(MockRestServiceServer mockServer, User user) {
        mockGetListWithOneElement(mockServer, GetUri(API_USERS), user);
    }
    
    public static void mockGetUserById(MockRestServiceServer mockServer, UUID id, Object expectedResult) {
    	mockGet(mockServer, GetUriById(API_USERS, id), expectedResult);
    }

    private static <T> void mockGetListWithOneElement(MockRestServiceServer mockServer, String expectedUri, T oneResult) {
    	List<T> expectedResult = oneResult != null ? List.of(oneResult) : List.of();
		mockGet(mockServer, expectedUri, expectedResult);
	}

	private static <T> void mockGet(MockRestServiceServer mockServer, String expectedUri, Object expectedResult) {
		try {
			mockServer.expect(ExpectedCount.manyTimes(), requestTo(expectedUri))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(expectedResult)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
	}
}
