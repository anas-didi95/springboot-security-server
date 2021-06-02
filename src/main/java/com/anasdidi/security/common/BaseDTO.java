package com.anasdidi.security.common;

public abstract class BaseDTO {

  public final String sessionId;

  public BaseDTO(String sessionId) {
    this.sessionId = sessionId;
  }
}
