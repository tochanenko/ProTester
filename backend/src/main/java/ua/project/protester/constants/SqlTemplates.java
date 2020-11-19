package ua.project.protester.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlTemplates {
    public static final String SAVE_TOKEN = "INSERT INTO tokens(user_id, token_value, token_expiry_date) "
            + "VALUES (:userId, :value, :expiryDate) ";
    public static final String FIND_TOKEN_EXPIRY_DATE_BY_TOKEN_VALUE = "SELECT token_expiry_date "
            + "FROM tokens "
            + "WHERE token_value = :value";

    public static final String FIND_USER_EMAIL_BY_TOKEN_VALUE = "SELECT u.user_email "
            + "FROM tokens t "
            + "LEFT JOIN users u USING(user_id) "
            + "WHERE t.token_value = :value";
    public static final String FIND_USER_BY_EMAIL = "SELECT "
            + "u.user_id, u.user_name, u.user_password, u.user_email, u.user_active, u.user_full_name "
            + "FROM users u "
            + "WHERE u.user_email = :email";
    public static final String UPDATE_USER_PASSWORD = "UPDATE users "
            + "SET user_password = :password "
            + "WHERE user_id = :id ";
}
