package ua.project.protester.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlTemplates {
    public static final String SAVE_TOKEN = "INSERT INTO tokens(user_id, token_value, token_expiry_date) "
            + "VALUES (?, ?, ?) ";
    public static final String FIND_TOKEN_EXPIRY_DATE_BY_TOKEN_VALUE = "SELECT token_expiry_date "
            + "from tokens "
            + "where token_value = ?";

    public static final String FIND_USER_EMAIL_BY_TOKEN_VALUE = "SELECT u.user_email "
            + "from tokens t "
            + "left join users u using(user_id) "
            + "where t.token_value = ?";
    public static final String FIND_USER_BY_EMAIL = "SELECT "
            + "u.user_id, u.user_name, u.user_password, u.user_email, u.user_active, u.user_full_name "
            + "from users u "
            + "where u.user_email = ?";
    public static final String UPDATE_USER_PASSWORD = "update users "
            + "set user_password = ? "
            + "where user_id = ? ";
}
