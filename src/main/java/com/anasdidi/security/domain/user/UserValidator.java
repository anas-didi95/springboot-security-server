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
    isBlank(errorList, username, "Username is mandatory field!");

    String password = dto.password;
    isBlank(errorList, password, "Password is mandatory field!");


    String fullName = dto.fullName;
    isBlank(errorList, fullName, "Full Name is mandatory field!");

    String email = dto.email;
    isBlank(errorList, email, "Email is mandatory field!");

    return errorList;
  }

  private List<String> validateUpdate(UserDTO dto) {
    List<String> errorList = new ArrayList<>();

    String id = dto.id;
    isBlank(errorList, id, "Id is mandatory field!");

    String fullName = dto.fullName;
    isBlank(errorList, fullName, "Full Name is mandatory field!");

    String email = dto.email;
    isBlank(errorList, email, "Email is mandatory field!");

    return errorList;
  }
}
