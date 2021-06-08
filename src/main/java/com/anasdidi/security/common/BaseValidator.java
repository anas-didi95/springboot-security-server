package com.anasdidi.security.common;

import java.util.List;
import reactor.core.publisher.Mono;

public abstract class BaseValidator<T> {

  protected final String ERROR_CODE = "E002";
  protected final ApplicationMessage message;

  public enum Action {
    CREATE, UPDATE
  }

  protected BaseValidator(ApplicationMessage message) {
    this.message = message;
  }

  protected void isBlank(List<String> errorList, String value, String errorMessage) {
    if (value == null || value.isBlank()) {
      errorList.add(errorMessage);
    }
  }

  protected abstract Mono<T> validate(Action action, T dto);
}
