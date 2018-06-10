package com.trak.sam.collegelog.config;

public class Config {
    public static final String BASE_URL = "http://192.168.43.80:3000/api/v1/";
    public static final String USER_ENDPOINT = "user";
    public static final String USER_ADMIN_ENDPOINT = "user/admin";
    public static final String ANONYMOUS_USER = "anonymous";
    public static final String ANONYMOUS_PASSWORD = "root";
    public static final String USER_DATA_PREFERENCE = "sam.user";
    public static final String DEPARTMENT_ENDPOINT = "department";
    public static final String ROLE_ENDPOINT = "role";
    public static final String SCHOOL_ENDPOINT = "school";
    public static final String ANONYMOUS_ROLE = "Anonymous";
    public static final String LIST_ENDPOINT = "list";

    public static class UserFilter {
        public static final String ROLE_TYPE = "roleType";
        public static final String OFFSET = "offset";
        public static final String LIMIT = "limit";
    }
}
