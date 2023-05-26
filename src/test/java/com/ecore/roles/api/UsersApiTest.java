package com.ecore.roles.api;

import static com.ecore.roles.utils.MockUtils.mockGetUserById;
import static com.ecore.roles.utils.MockUtils.mockGetUsers;
import static com.ecore.roles.utils.RestAssuredHelper.getUser;
import static com.ecore.roles.utils.RestAssuredHelper.getUsers;
import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static com.ecore.roles.utils.TestData.GIANNI_USER_UUID;
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

import com.ecore.roles.client.model.User;
import com.ecore.roles.constants.ValidationConstants;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.utils.TestData;
import com.ecore.roles.web.dto.UserDto;

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
    void shouldGetAllUsers() {
        User expectedUser = TestData.GIANNI_USER();

        mockGetUsers(mockServer, expectedUser);
        
        UserDto[] actualUsers = getUsers()
                .statusCode(200)
                .extract().as(UserDto[].class);

        assertThat(actualUsers.length).isEqualTo(1);
        assertThat(actualUsers[0]).isEqualTo(UserDto.fromModel(expectedUser));
    }
    
    @Test
    void shouldGetAllUsersButReturnsEmptyList() {
        mockGetUsers(mockServer, null);
        
        UserDto[] actualUsers = getUsers()
                .statusCode(200)
                .extract().as(UserDto[].class);

        assertThat(actualUsers.length).isEqualTo(0);
    }

    @Test
    void shouldGetUserById() {
    	User expectedUser = GIANNI_USER();
		mockGetUserById(mockServer, GIANNI_USER_UUID, expectedUser);
        
		getUser(GIANNI_USER_UUID)
	        .statusCode(200)
	        .body("id", equalTo(expectedUser.getId().toString()))
	        .body("firstName", equalTo(expectedUser.getFirstName()));
    }
    
    @Test
    void shouldFailToGetUserById() {
    	mockGetUserById(mockServer, GIANNI_USER_UUID, null);
        getUser(GIANNI_USER_UUID)
        		.validate(404, format(ValidationConstants.USER_NOT_FOUND, GIANNI_USER_UUID));
    }
    
}
