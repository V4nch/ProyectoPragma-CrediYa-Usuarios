package co.com.pragma.powerup.model.user.utils;

import java.util.regex.Pattern;

public class Constants {

    // ---------------------------
    // VALIDATION CONSTANTS
    // ---------------------------
    public static final String USER_NULL = "The user cannot be null";
    public static final String NAME_REQUIRED  = "Name is required";
    public static final String LASTNAME_REQUIRED  = "Lastname is required";
    public static final String EMAIL_REQUIRED  = "Email is required";
    public static final String BASE_SALARY_REQUIRED  = "Base salary is required";
    public static final String INVALID_FORMAT  = "Invalid email format";
    public static final String SALARY_RANGE = "Base salary must be between 0 and 15,000,000";
    public static final String SALARY_IS_NUMERIC = "Base salary must be numeric";
    public static final String GIVEN_EMAIL_ALREADY_EXIST = "A user with the given email already exists: ";
    // ---------------------------
    // PATTERNS
    // ---------------------------
    public static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ---------------------------
    // LOGGING MESSAGES
    // ---------------------------
    public static final String LOG_VALIDATE_USER  = "Starting validation for user attributes";
    public static final String LOG_USER_CREATE_SUCCESSFUL = "User successfully created with email={}";
    public static final String LOG_USER_CREATE_ERROR = "Error creating user with email={}: {}";
    public static final String LOG_USER_CREATE_RECEIVED = "User creation request received";
    public static final String LOG_BUSINESS_ERROR = "BUSINESS ERROR: {}";
    public static final String LOG_EMAIL_ALREADY_EXIST = "Error, a user with the given email already exists: {}";
    public static final String LOG_DB_INTEGRITY_ERROR  = "INTEGRITY ERROR IN DB: {}";
    public static final String LOG_DATA_ACCESS_ERROR = "Data access error: {}";
    public static final String LOG_UNEXPECTED_ERROR = "Unexpected error: {}";
    public static final String LOG_RECEIVED_DATA = "Received data: {}";
    public static final String LOG_USER_CREATED = "User created: {}";
    public static final String LOG_USER_CREATION_ERROR = "Error creating user: {}";



    // ---------------------------
    // HTTP ERROR MESSAGES
    // ---------------------------
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String DATABASE_ERROR = "DATABASE_ERROR";
    public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
    public static final String DB_VIOLATION_MESSAGE = "Database constrain violation";
    public static final String DB_ACCESS_ERROR = "Database access error";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String UNEXPECTED_ERROR = "An unexpected error has occurred";

    // ---------------------------
    // HTTP
    // ---------------------------
    public static final String PATH_USER = "/api/v1/usuarios";
}
