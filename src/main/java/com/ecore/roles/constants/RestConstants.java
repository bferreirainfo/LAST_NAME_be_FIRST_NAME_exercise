package com.ecore.roles.constants;

public class RestConstants {

    // Generic Stuff
    public static final String APPLICATION_JSON = "application/json";

    // Versions
    public static final String V1 = "/v1/";

    // API Names
    public static final String API_ROLES = "roles";
    public static final String API_USERS = "users";
    public static final String API_TEAMS = "teams";

    // Reusable actions
    public static final String ACTION_SEARCH = "/search";

    // Rest Fields
    public static final String FIELD_ROLE_ID = "roleId";
    public static final String PATH_ROLE_ID = "/{" + FIELD_ROLE_ID + "}";
    public static final String FIELD_USER_ID = "userId";
    public static final String PATH_USER_ID = "/{" + FIELD_USER_ID + "}";
    public static final String FIELD_TEAM_ID = "teamId";
    public static final String PATH_TEAM_ID = "/{" + FIELD_TEAM_ID + "}";

    // Mappings
    public static final String V1_ROLES = V1 + API_ROLES;

    public static final String V1_ROLES_ROLE_ID = V1_ROLES + PATH_ROLE_ID;
    public static final String V1_ROLES_SEARCH = V1_ROLES + ACTION_SEARCH;

    public static final String V1_ROLES_MEMBERSHIPS = V1_ROLES + "/memberships";
    public static final String V1_ROLES_MEMBERSHIPS_SEARCH = V1_ROLES_MEMBERSHIPS + ACTION_SEARCH;

    public static final String V1_USERS = V1 + API_USERS;
    public static final String V1_USERS_USER_ID = V1_USERS + PATH_USER_ID;

    public static final String V1_TEAMS = V1 + API_TEAMS;
    public static final String V1_TEAMS_TEAM_ID = V1_TEAMS + PATH_TEAM_ID;

}
