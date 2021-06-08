package com.anasdidi.security.domain.user;

import java.util.ArrayList;
import java.util.List;
import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;
import com.anasdidi.security.common.BaseValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class UserValidator extends BaseValidator<UserDTO> {

  private static final Logger logger = LogManager.getLogger(UserValidator.class);

  @Autowired
  public UserValidator(ApplicationMessage message) {
    super(message);
  }

  @Override
  protected Mono<UserDTO> validate(Action action, UserDTO dto) {
    final String TAG = "validate";
    List<String> errorList = new ArrayList<>();

    switch (action) {
      case CREATE:
        errorList = validateCreate(dto);
        break;
      case UPDATE:
        errorList = validateUpdate(dto);
        break;
    }

    if (!errorList.isEmpty()) {
      logger.error("[{}] validate={}, {}", TAG, action, dto.toString());
      return Mono.error(
          new ApplicationException(ERROR_CODE, message.getErrorMessage(ERROR_CODE), errorList));
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

  private List<String> validateUpdate(UserDTO dto) {
    List<String> errorList = new ArrayList<>();
    String id = dto.id;
    String fullName = dto.fullName;
    String email = dto.email;

    if (id == null || id.isBlank()) {
      errorList.add("Id is mandatory field!");
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
