package com.ecore.roles.utils;

import static com.ecore.roles.constants.RestConstants.FIELD_ROLE_ID;
import static com.ecore.roles.constants.RestConstants.FIELD_TEAM_ID;
import static com.ecore.roles.constants.RestConstants.FIELD_USER_ID;
import static com.ecore.roles.constants.RestConstants.V1_ROLES;
import static com.ecore.roles.constants.RestConstants.V1_ROLES_MEMBERSHIPS;
import static com.ecore.roles.constants.RestConstants.V1_ROLES_MEMBERSHIPS_SEARCH;
import static com.ecore.roles.constants.RestConstants.V1_ROLES_ROLE_ID;
import static com.ecore.roles.constants.RestConstants.V1_ROLES_SEARCH;
import static com.ecore.roles.constants.RestConstants.V1_TEAMS;
import static com.ecore.roles.constants.RestConstants.V1_TEAMS_TEAM_ID;
import static com.ecore.roles.constants.RestConstants.V1_USERS;
import static com.ecore.roles.constants.RestConstants.V1_USERS_USER_ID;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;

import java.util.UUID;

import org.hamcrest.Matchers;

import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.web.dto.MembershipDto;
import com.ecore.roles.web.dto.RoleDto;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class RestAssuredHelper {

    public static void setUp(int port) {
        RestAssured.reset();
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.baseURI = "http://localhost:" + port;
    }

    public static EcoreValidatableResponse sendRequest(ValidatableResponse validatableResponse) {
        return new EcoreValidatableResponse(validatableResponse);
    }

    public static EcoreValidatableResponse createRole(Role role) {
        return sendRequest(givenNullableBody(RoleDto.fromModel(role))
                .contentType(JSON)
                .when()
                .post(V1_ROLES)
                .then());
    }

    public static EcoreValidatableResponse getRoles() {
        return sendRequest(when()
                .get(V1_ROLES)
                .then());
    }

    public static EcoreValidatableResponse getRole(UUID roleId) {
        return sendRequest(given()
                .pathParam(FIELD_ROLE_ID, roleId)
                .when()
                .get(V1_ROLES_ROLE_ID)
                .then());
    }

    public static EcoreValidatableResponse getRole(UUID userID, UUID teamID) {
        return sendRequest(given()
                .queryParam(FIELD_USER_ID, userID)
                .queryParam(FIELD_TEAM_ID, teamID)
                .when()
                .get(V1_ROLES_SEARCH)
                .then());
    }

    public static EcoreValidatableResponse createMembership(Membership membership) {
        return sendRequest(givenNullableBody(MembershipDto.fromModel(membership))
                .contentType(JSON)
                .when()
                .post(V1_ROLES_MEMBERSHIPS)
                .then());
    }

    public static EcoreValidatableResponse getMemberships(UUID roleId) {
        return sendRequest(given()
                .queryParam(FIELD_ROLE_ID, roleId)
                .when()
                .get(V1_ROLES_MEMBERSHIPS_SEARCH)
                .then());
    }

    public static EcoreValidatableResponse getTeams() {
        return sendRequest(when()
                .get(V1_TEAMS)
                .then());
    }

    public static EcoreValidatableResponse getTeam(UUID teamId) {
        return sendRequest(given()
                .pathParam(FIELD_TEAM_ID, teamId)
                .when()
                .get(V1_TEAMS_TEAM_ID)
                .then());
    }

    public static EcoreValidatableResponse getUsers() {
        return sendRequest(when()
                .get(V1_USERS)
                .then());
    }

    public static EcoreValidatableResponse getUser(UUID userId) {
        return sendRequest(given()
                .pathParam(FIELD_USER_ID, userId)
                .when()
                .get(V1_USERS_USER_ID)
                .then());
    }

    private static RequestSpecification givenNullableBody(Object object) {
        RequestSpecification requestSpecification = given();
        if (object != null) {
            requestSpecification = requestSpecification.body(object);
        }
        return requestSpecification;
    }

    public static class EcoreValidatableResponse {

        ValidatableResponse validatableResponse;

        public EcoreValidatableResponse(ValidatableResponse validatableResponse) {
            this.validatableResponse = validatableResponse;
        }

        public void validate(int status, String message) {
            this.validatableResponse
                    .statusCode(status)
                    .body("status", Matchers.equalTo(status))
                    .body("error", Matchers.equalTo(message));
        }

        public ValidatableResponse statusCode(int statusCode) {
            return this.validatableResponse
                    .statusCode(statusCode);
        }

        public ExtractableResponse<Response> extract() {
            return this.validatableResponse
                    .extract();
        }

    }
}
