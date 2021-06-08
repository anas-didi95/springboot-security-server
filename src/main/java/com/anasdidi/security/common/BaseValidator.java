package com.anasdidi.security.common;

import reactor.core.publisher.Mono;

public abstract class BaseValidator<T> {

  protected final String ERROR_CODE = "E002";
  protected final ApplicationMessage message;

  protected BaseValidator(ApplicationMessage message) {
    this.message = message;
  }

  protected abstract Mono<T> validate(Action action, T dto);

  public enum Action {
    CREATE, UPDATE
  }
}
