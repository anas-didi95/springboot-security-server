package com.anasdidi.security.common;

import reactor.core.publisher.Mono;

public abstract class BaseValidator<T> {

  protected final String ERROR_CODE = "E001";
  protected final ApplicationConstants constants;

  protected BaseValidator(ApplicationConstants constants) {
    this.constants = constants;
  }

  protected abstract Mono<T> validate(Action action, T dto);

  public enum Action {
    CREATE
  }
}
