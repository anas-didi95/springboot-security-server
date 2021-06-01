package com.anasdidi.security.domain.user;

import java.util.ArrayList;
import java.util.List;
import com.anasdidi.security.common.ApplicationConstants;
import com.anasdidi.security.common.ApplicationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class UserValidator {

  private static final Logger logger = LogManager.getLogger(UserValidator.class);
  private final String ERROR_CODE = "E001";
  private final ApplicationConstants constants;

  public enum Action {
    CREATE
  }

  public UserValidator(ApplicationConstants constants) {
    this.constants = constants;
  }

  public Mono<UserDTO> validate(Action action, UserDTO dto) {
    final String TAG = "validate";
    List<String> errorList = new ArrayList<>();

    switch (action) {
      case CREATE:
        errorList = validateCreate(dto);
        break;
    }

    if (!errorList.isEmpty()) {
      logger.error("[{}] validate={}, {}", TAG, action, dto.toString());
      return Mono.error(
          new ApplicationException(ERROR_CODE, constants.getErrorMessage(ERROR_CODE), errorList));
    }

    return Mono.just(dto);
  }

  private List<String> validateCreate(UserDTO dto) {
    List<String> errorList = new ArrayList<>();
    String username = dto.username;
    String password = dto.password;
    String fullName = dto.fullName;
    String email = dto.email;

    if (username == null || username.isBlank()) {
      errorList.add("Username is mandatory field!");
    }

    if (password == null || password.isBlank()) {
      errorList.add("Password is mandatory field!");
    }

    if (fullName == null || fullName.isBlank()) {
      errorList.add("Full Name is mandatory field!");
    }

    if (email == null || email.isBlank()) {
      errorList.add("Email is mandatory field!");
    }

    return errorList;
  }
}
