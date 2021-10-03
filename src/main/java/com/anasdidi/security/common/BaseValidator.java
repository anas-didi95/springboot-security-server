package com.anasdidi.security.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reactor.core.publisher.Mono;

public abstract class BaseValidator<T extends BaseDTO> {

  private static final Logger logger = LogManager.getLogger(BaseValidator.class);
  protected final String ERROR_CODE = "E002";
  protected final ApplicationMessage message;

  public enum Action {
    CREATE, UPDATE, LOGIN
  }

  protected BaseValidator(ApplicationMessage message) {
    this.message = message;
  }

  protected List<String> validateCreate(T dto) {
    return null;
  }

  protected List<String> validateUpdate(T dto) {
    return null;
  }

  protected List<String> validateLogin(T dto) {
    return null;
  }

  public final Mono<T> validate(Action action, T dto) {
    List<String> errorList = new ArrayList<>();

    switch (action) {
      case CREATE:
        errorList = validateCreate(dto);
        break;
      case UPDATE:
        errorList = validateUpdate(dto);
        break;
      case LOGIN:
        errorList = validateLogin(dto);
        break;
    }

    if (errorList == null) {
      logger.warn("[validate:{}] action={}, vo={}", dto.sessionId, action, dto);
      logger.warn("[validate:{}] Validation not implemented!", dto.sessionId);
    } else if (!errorList.isEmpty()) {
      logger.error("[validate:{}] validate={}, {}", dto.sessionId, action, dto.toString());
      return Mono.error(new ApplicationException(ERROR_CODE, message.getErrorMessage(ERROR_CODE), errorList));
    }

    return Mono.just(dto);
  }

  protected final void isBlank(List<String> errorList, String value, String errorMessage) {
    if (value == null || value.isBlank()) {
      errorList.add(errorMessage);
    }
  }

  protected final void isBlank(List<String> errorList, Object value, String errorMessage) {
    if (value == null) {
      errorList.add(errorMessage);
    }
  }
}
