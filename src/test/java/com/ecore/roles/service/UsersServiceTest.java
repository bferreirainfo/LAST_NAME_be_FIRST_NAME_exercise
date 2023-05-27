package com.ecore.roles.service;

import static com.ecore.roles.constants.ValidationConstants.USER_NOT_FOUND;
import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static com.ecore.roles.utils.TestData.UUID_1;
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

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.impl.UsersServiceImpl;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersServiceImpl usersService;
    @Mock
    private UsersClient usersClient;

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = GIANNI_USER();
        when(usersClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(gianniUser));

        assertNotNull(usersService.getUser(UUID_1));
    }
    
    @Test
    void shouldFailWhenUserIdIsInvalid() {
        User gianniUser = GIANNI_USER();
        when(usersClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(null));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
    		usersService.getUser(UUID_1);
    	});
        
        assertEquals(exception.getMessage(), format(USER_NOT_FOUND, UUID_1));
    }
    
    @Test
    void shouldGetUsers() {
        List<User> expectedResult = List.of(GIANNI_USER());
		when(usersClient.getUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(expectedResult));

        List<User> actualResult = usersService.getUsers();
        
        assertEquals(actualResult, expectedResult);
    }
}
