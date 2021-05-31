package com.anasdidi.security.domain.user;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class UserValidator {

  public enum Action {
    CREATE
  }

  public Mono<UserDTO> validate(Action action, UserDTO dto) {
    List<String> errorList = new ArrayList<>();

    switch (action) {
      case CREATE:
        errorList = validateCreate(dto);
        break;
    }

    if (!errorList.isEmpty()) {
      return Mono.error(new Exception("Validation Error"));
    }

    return Mono.just(dto);
  }

  private List<String> validateCreate(UserDTO dto) {
    List<String> errorList = new ArrayList<>();
    String username = dto.username;

    if (username == null || username.isBlank()) {
      errorList.add("Username is mandatory field!");
    }

    return errorList;
  }
}
