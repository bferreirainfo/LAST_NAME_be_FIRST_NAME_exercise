package com.ecore.roles.constants;

public class ValidationConstants {

    // Business Message
    public static final String ROLE_NOT_FOUND = "Role %s not found";
    public static final String ROLE_ALREADY_EXISTS = "Role already exists";
    public static final String ROLE_NOT_FOUND_FOR_USER_AND_TEAM =
            "Role not found fpr userID: %s and TeamID: %s ";
    public static final String INVALID_ROLE_OBJECT =
    		"Invalid 'Role' object";

    public static final String TEAM_NOT_FOUND = "Team %s not found";
    public static final String USER_NOT_FOUND = "User %s not found";

    public static final String MEMBERSHIP_ALREADY_EXISTS = "Membership already exists";
    public static final String INVALID_MEMBERSHIP_OBJECT =
            "Invalid 'Membership' object. The provided user doesn't belong to the provided team.";

    // Rest Messages
    public static final String BAD_REQUEST = "Bad Request";
    public static final String NOT_FOUND = "Not Found";
}
