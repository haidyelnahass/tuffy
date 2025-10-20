package com.eg.user.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String USER_ALREADY_EXISTS_MESSAGE = "A user with this data already exists";

  public static final String CUSTOMER_STATUS_NOT_FOUND = "Could not find customer status";

  public static final String USER_TYPE_NOT_FOUND = "Could not find user type";

  public static final String MISSING_VEHICLE_DETAILS = "Vehicle Details are missing";

  public static final String USER_NOT_FOUND_MESSAGE = "User not found";

  public static final String USER_ALREADY_CONFIRMED_HIS_EMAIL = "Email already confirmed";

  public static final String WRONG_CONFIRMATION_CODE = "Wrong confirmation code inserted";

  public static final String USER_NOT_ACTIVE = "Cannot initiate login for inactive user";

  public static final String INCORRECT_PASSWORD_ENTERED = "The password entered is incorrect";

  public static final String WRONG_PASSWORD_ENTERED_TOO_MANY_TIMES = "Wrong password entered too many times, "
    + "account is now blocked";

  public static final String ONLY_EMAIL_OR_PHONE_SHOULD_BE_SENT =
    "Only email or phone should be sent in the request body";

  public static final String WRONG_TOKEN_RECEIVED
    = "Wrong token received";

  public static final String TOKEN_EXPIRED
    = "Token expired";

  public static final String TOKEN_TYPE = "Bearer";


  // MAIL
  public static final String CONFIRMATION_CODE_SUBJECT = "Confirmation Code - Tuffy";
  public static final String CONFIRMATION_CODE_TEXT = "Your confirmation code is <confirmation_code>. Please use it "
    + "within the next 10 minutes";

  public static final String RESET_PASSWORD_SUBJECT = "Reset Password - Tuffy";

  public static final String RESET_PASSWORD_TEXT = "To reset your password, please follow this link: <reset_link>, this "
    + "link will be valid for the next 10 minutes";


  // HEADERS
  public static final String USER_ID_HEADER = "UserId";

  public static final String FAILED_TO_SEND_EMAIL = "Failed to send email";
}
