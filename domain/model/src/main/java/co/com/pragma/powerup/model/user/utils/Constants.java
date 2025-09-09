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
    public static final String GIVEN_ID_CARD_ALREADY_EXIST = "A user with the given id card already exists: ";
    public static final String API_CREDIYA = "API CrediYa";
    public static final String VERSION_1 = "1.0";
    public static final String USER_DESCRIPTION = "CrediYa Authentication and User Management API";
    public static final String NULL = "null";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ID = "id";
    public static final String ID_PARAMS = "/{id}";
    public static final String USER_ID_DESCRIPTION = "ID del usuario";
    public static final String ID_EXAMPLE = "1234";
    public static final String ROLE_1 = "ROLE_";
    public static final String ROLES= "roles";
    public static final String SECURITY_SECRET = "${security.jwt.secret}";
    public static final String SECURITY_EXPIRATION = "${security.jwt.expiration}";
    public static final String QUERY_USER_AUTH = "SELECT u.email_address, u.password,r.name AS role_name  " +
            "FROM users u JOIN roles r ON u.id_role = r.id_role " +
            "WHERE u.email_address = :email_address";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String BEARER_AUTH = "BearerAuth";
    public static final String BEARER = "bearer";
    public static final String BEARER_SPACE = "Bearer ";
    public static final String JWT = "JWT";
    public static final String AUTHORIZATION = "Authorization";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String SWAGGER_UI_ALL = "/swagger-ui/**";
    public static final String V3_API_DOCS = "/v3/api-docs/**";
    public static final String WEBJARS = "/webjars/**";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ADVISOR = "ASESOR";
    public static final String ROLE_CLIENT = "CLIENTE";


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
    public static final String LOG_ID_CARD_ALREADY_EXIST = "Error, a user with the given id card already exists: {}";
    public static final String LOG_DB_INTEGRITY_ERROR  = "INTEGRITY ERROR IN DB: {}";
    public static final String LOG_DATA_ACCESS_ERROR = "Data access error: {}";
    public static final String LOG_UNEXPECTED_ERROR = "Unexpected error: {}";
    public static final String LOG_RECEIVED_DATA = "Received data: {}";
    public static final String LOG_USER_CREATED = "User created: {}";
    public static final String LOG_USER_CREATION_ERROR = "Error creating user: {}";
    public static final String LOG_USER_GET_SUCCESSFUL = "User successfully got with id card={}";
    public static final String LOG_USER_GET_ERROR = "Error getting user with id card={}: {}";
    public static final String LOG_USER_GET_RECEIVED = "User get request received";
    public static final String LOG_USER_GET = "User got: {}";
    public static final String LOG_USER_GET_ERROR_HANDLER = "Error getting user: {}";
    public static final String LOG_USER_NOT_FOUND = "Error, a user with the given id card don't exists: {}";
    public static final String LOG_LOGIN_ATTEMPT = "Attempting login for email: {}";
    public static final String LOG_AUTHENTICATING = "Authenticating user: {}";
    public static final String LOG_AUTH_SUCCESS = "User {} authenticated successfully with roles: {}";
    public static final String LOG_BUILDING_CLAIMS = "Building claims for user with idCard: {}";
    public static final String LOG_TOKEN_GENERATED = "Token generated successfully for user: {}";
    public static final String LOG_LOGIN_COMPLETED = "Login completed for {}";
    public static final String LOG_AUTH_FAILED = "Authentication failed for {}: {}";
    public static final String EXC_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String EXC_USER_NOT_FOUND = "User not found with email: ";
    public static final String LOG_INVALID_CREDENTIALS = "Invalid email or password: {}";
    // ---------------------------
    // HTTP ERROR MESSAGES
    // ---------------------------
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String DATABASE_ERROR = "DATABASE_ERROR";
    public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
    public static final String ID_CARD_ALREADY_EXISTS = "ID_CARD_ALREADY_EXISTS";
    public static final String DB_VIOLATION_MESSAGE = "Database constrain violation";
    public static final String DB_ACCESS_ERROR = "Database access error";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String UNEXPECTED_ERROR = "An unexpected error has occurred";
    public static final String NOT_FOUND = "USER_NOT_FOUND";

    // ---------------------------
    // HTTP
    // ---------------------------
    public static final String PATH_USER = "/api/v1/usuarios";
    public static final String PATH_LOGIN = "/api/v1/login";
    public static final String CONTENT_TYPE = "application/json";
    public static final String NAME_FUNCTION = "createUser";
    public static final String NAME_FUNCTION_GET ="getUser";
    public static final String NAME_FUNCTION_LOGIN = "login";
    public static final String CODE_200 = "200";
    public static final String CODE_400 = "400";
    public static final String CODE_401 = "401";
    public static final String CODE_404 = "404";
    public static final String CODE_409 = "409";
    public static final String CODE_500 = "500";
    // ---------------------------
    // ROUTER OPERATION
    // ---------------------------
    public static final String SUMMARY_REGISTER_USER = "Register user";
    public static final String DESCRIPTION_REGISTER_USER = "Allows registering a new user";
    public static final String SUMMARY_LOGIN_USER = "Login user";
    public static final String DESCRIPTION_LOGIN_USER = "Allows login a user";

    // ---------------------------
    // SUCCESS RESPONSES
    // ---------------------------
    public static final String RESPONSE_USER_REGISTERED = "User registered";
    public static final String RESPONSE_USER_LOGGED = "User logged";
    public static final String EXAMPLE_USER_GET_NAME = "Got user";
    public static final String EXAMPLE_USER_REGISTERED_NAME = "Registered user";
    public static final String EXAMPLE_USER_REGISTERED_VALUE = """
            {
              "idCard": "1094123321",
              "name": "Ivan",
              "lastName": "Moreno",
              "birthDate": "1995-12-22",
              "address": "Avenida siempre viva",
              "phoneNumber": "3211451234",
              "emailAddress": "Ivan@Gm.com",
              "baseSalary": "40981"
            }
            """;
    public static final String EXAMPLE_USER_REQUEST_VALUE = """
            {
              "idCard": "1094123321",
              "name": "Ivan",
              "lastName": "Moreno",
              "birthDate": "1995-12-22",
              "address": "Avenida siempre viva",
              "phoneNumber": "3211451234",
              "emailAddress": "Ivan@Gm.com",
              "baseSalary": "40981",
              "password": "1234",
              "roleName": "admin"
            }
            """;
    public static final String EXAMPLE_LOGIN_NAME = "Logged user";
    public static final String EXAMPLE_LOGIN_VALUE = """
              {
                  "email": "Ivanchio@Gm.com",
                  "password": "temporal123"
              }
            """;
    public static final String EXAMPLE_LOGIN_RESPONSE_NAME = "Token";
    public static final String EXAMPLE_LOGIN_RESPONSE_VALUE = """
    {
        "token": "eyJhbGciOiJIUzI1NiJ9.YXQiOjE3NTc0NDg0NjUsImV4cCI6MTc1NzQ1MjA2NX0.2bNkST0gCdhpcz2-Xt4"
    }""";
    // ---------------------------
    // ERROR RESPONSES
    // ---------------------------
    public static final String RESPONSE_BAD_REQUEST = "Invalid data";
    public static final String RESPONSE_CONFLICT = "Email already exists";
    public static final String RESPONSE_INTERNAL_ERROR = "Unexpected error";
    public static final String RESPONSE_INVALID_CREDENTIALS = "Invalid email or password";

    // ---------------------------
    // ERROR EXAMPLES
    // ---------------------------
    public static final String EXAMPLE_INVALID_CREDENTIALS_NAME = "Invalid credentials error";
    public static final String EXAMPLE_INVALID_CREDENTIALS = """
            {
              "code": "UNAUTHORIZED",
              "message": "Invalid email or password"
            }
            """;
    public static final String EXAMPLE_INVALID_SALARY_NAME = "Invalid salary error";
    public static final String EXAMPLE_INVALID_SALARY_VALUE = """
            {
              "code": "BAD_REQUEST",
              "message": "Base salary must be between 0 and 15,000,000"
            }
            """;

    public static final String EXAMPLE_DUPLICATE_EMAIL_NAME = "Duplicate email error";
    public static final String EXAMPLE_DUPLICATE_EMAIL_VALUE = """
            {
              "code": "CONFLICT",
              "message": "Email already exists"
            }
            """;
    public static final String EXAMPLE_USER_NOT_FOUND_NAME = "User not found";
    public static final String EXAMPLE_USER_NOT_FOUND_VALUE = """
            {
              "code": "NOT_FOUND",
              "message": "User not found"
            }
            """;

    public static final String EXAMPLE_SERVER_ERROR_NAME = "Server error";
    public static final String EXAMPLE_SERVER_ERROR_VALUE = """
            {
              "code": "INTERNAL_SERVER_ERROR",
              "message": "Unexpected error occurred"
            }
            """;
}
